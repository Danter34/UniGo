package com.example.project_unigo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;
import com.example.project_unigo.model.QuestionModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class QuestionActivity extends AppCompatActivity {

    private Spinner spnTopic;
    private EditText edtContent;
    private Button btnSend;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        db = FirebaseFirestore.getInstance();
        initView();
        setupSpinner();

        // Xử lý nút Back
        findViewById(R.id.btnBackQuestion).setOnClickListener(v -> finish());

        // Xử lý nút Gửi
        btnSend.setOnClickListener(v -> handleSendQuestion());
    }

    private void initView() {
        spnTopic = findViewById(R.id.spnTopic);
        edtContent = findViewById(R.id.edtContent);
        btnSend = findViewById(R.id.btnSendQuestion);
    }

    private void setupSpinner() {
        // Danh sách các lý do
        String[] topics = {
                "Tư vấn tuyển sinh",
                "Thắc mắc về học phí",
                "Đăng ký môn học",
                "Cấp lại thẻ sinh viên",
                "Hoạt động ngoại khóa",
                "Vấn đề khác"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, topics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTopic.setAdapter(adapter);
    }

    private void handleSendQuestion() {
        String content = edtContent.getText().toString().trim();
        String selectedTopic = spnTopic.getSelectedItem().toString();

        // 1. Kiểm tra nhập liệu
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Vui lòng nhập nội dung câu hỏi", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Lấy MSSV người đang đăng nhập
        SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String currentMssv = prefs.getString("mssv", "Ẩn danh");

        // 3. Tạo Object
        QuestionModel question = new QuestionModel(currentMssv, selectedTopic, content, new Date(), "Đang chờ");

        // 4. Gửi lên Firebase
        db.collection("Questions")
                .add(question)
                .addOnSuccessListener(documentReference -> {
                    showSuccessDialog();
                    edtContent.setText(""); // Xóa nội dung sau khi gửi
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi gửi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_success, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        // Làm trong suốt nền Dialog để bo góc đẹp hơn
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Nút Đóng trong Dialog
        Button btnClose = view.findViewById(R.id.btnCloseDialog);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}