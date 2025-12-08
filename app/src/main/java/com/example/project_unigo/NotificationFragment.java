package com.example.project_unigo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.project_unigo.model.NotificationModel;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class NotificationFragment extends Fragment {

    private RecyclerView rcvNotification;
    private NotificationAdapter adapter;
    private List<NotificationModel> listData;
    private FirebaseFirestore db; // 1. Khai báo biến Database

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rcvNotification = view.findViewById(R.id.rcvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvNotification.setLayoutManager(linearLayoutManager);

        listData = new ArrayList<>();
        adapter = new NotificationAdapter(listData);
        rcvNotification.setAdapter(adapter);

        // 2. Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // 3. Gọi hàm lấy dữ liệu
        getDataFromFirebase();

        return view;
    }

    private void getDataFromFirebase() {
        // Truy cập vào collection "notifications" mà bạn vừa tạo trên web
        db.collection("notifications")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Xóa dữ liệu cũ (nếu có) để tránh bị trùng lặp khi load lại
                        listData.clear();

                        // Duyệt qua từng dòng dữ liệu (document) trả về
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy các trường title và date
                            String title = document.getString("title");
                            String date = document.getString("date");

                            // Thêm vào list
                            listData.add(new NotificationModel(title, date));
                        }

                        // Quan trọng: Báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại giao diện
                        adapter.notifyDataSetChanged();

                    } else {
                        // Nếu lỗi thì in ra Logcat để kiểm tra
                        Log.w("FireStore", "Lỗi lấy dữ liệu.", task.getException());
                    }
                });
    }
}