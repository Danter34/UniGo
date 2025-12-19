package com.example.unigoadmin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.unigoadmin.Adapter.AdminNewsAdapter;
import com.example.unigoadmin.R;
import com.example.unigoadmin.model.NewsModel;
import com.example.unigoadmin.model.NotificationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminNewsManagerActivity extends AppCompatActivity {

    // LẤY TỪ FILE JSON BẠN GỬI: "project_id": "unigo-38e30"
    private static final String PROJECT_ID = "unigo-38e30";

    private RecyclerView rcvNews;
    private FloatingActionButton fabAddNews;
    private List<NewsModel> listNews;
    private AdminNewsAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_manager);

        db = FirebaseFirestore.getInstance();
        initView();
        loadData();

        fabAddNews.setOnClickListener(v -> showAddNewsDialog());
    }

    private void initView() {
        rcvNews = findViewById(R.id.rcvAdminNews);
        fabAddNews = findViewById(R.id.fabAddNews);

        listNews = new ArrayList<>();
        adapter = new AdminNewsAdapter(this, listNews, this::deleteNews);

        rcvNews.setLayoutManager(new LinearLayoutManager(this));
        rcvNews.setAdapter(adapter);
    }

    private void loadData() {
        db.collection("news")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        listNews.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            NewsModel news = doc.toObject(NewsModel.class);
                            news.setDocumentId(doc.getId());
                            listNews.add(news);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void showAddNewsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_news, null);
        builder.setView(view);

        EditText edtContent = view.findViewById(R.id.edtNewsContent);
        EditText edtImage = view.findViewById(R.id.edtNewsImage);
        EditText edtLink = view.findViewById(R.id.edtNewsLink);
        Button btnPost = view.findViewById(R.id.btnPostNews);

        AlertDialog dialog = builder.create();

        btnPost.setOnClickListener(v -> {
            String content = edtContent.getText().toString().trim();
            String imgUrl = edtImage.getText().toString().trim();
            String link = edtLink.getText().toString().trim();
            Date now = new Date();

            if (content.isEmpty()) {
                Toast.makeText(this, "Nội dung không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            NewsModel news = new NewsModel(content, imgUrl, now, link);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateStr = sdf.format(now);
            NotificationModel noti = new NotificationModel(content, dateStr);

            // 1. Lưu News
            db.collection("news").add(news)
                    .addOnSuccessListener(docRef -> {
                        // 2. Lưu Notification
                        db.collection("notifications").add(noti)
                                .addOnSuccessListener(docRef2 -> {

                                    // 3. Gửi Push Notification (FCM HTTP v1)
                                    sendPushNotification("Thông báo từ UniGo", content);

                                    Toast.makeText(this, "Đăng tin thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        dialog.show();
    }

    public void deleteNews(String documentId) {
        db.collection("news").document(documentId).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã xóa tin", Toast.LENGTH_SHORT).show());
    }

    // LOGIC GỬI THÔNG BÁO (GOOGLE AUTH + VOLLEY)

    private void sendPushNotification(String title, String body) {
        new Thread(() -> {
            try {
                String accessToken = getAccessToken();
                if (accessToken == null) return;

                String url = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

                JSONObject message = new JSONObject();
                JSONObject msg = new JSONObject();

                // gửi theo topic
                msg.put("topic", "students");

                // phần thông báo để hiện lên status bar
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", body);
                msg.put("notification", notification);

                // phần android config
                JSONObject androidObj = new JSONObject();
                androidObj.put("priority", "high");
                msg.put("android", androidObj);

                // phần data (khi app đang chạy)
                JSONObject dataObj = new JSONObject();
                dataObj.put("title", title);
                dataObj.put("body", body);
                msg.put("data", dataObj);

                message.put("message", msg);

                runOnUiThread(() -> sendVolleyRequest(url, message, accessToken));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String getAccessToken() {
        try {
            // Đọc file res/raw/service_account.json
            InputStream stream = getResources().openRawResource(R.raw.service_account);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendVolleyRequest(String url, JSONObject jsonBody, String accessToken) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> Toast.makeText(this, "Đã gửi thông báo đến sinh viên!", Toast.LENGTH_SHORT).show(),
                error -> {
                    String err = "Lỗi gửi: " + error.getMessage();
                    if (error.networkResponse != null) {
                        try {
                            String body = new String(error.networkResponse.data, "UTF-8");
                            err = "Lỗi " + error.networkResponse.statusCode + ": " + body;
                        } catch (Exception e) {}
                    }
                    Toast.makeText(this, err, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                // Quan trọng: Dùng Bearer Token
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}