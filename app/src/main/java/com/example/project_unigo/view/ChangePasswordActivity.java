package com.example.project_unigo.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText edtOldPass, edtNewPass, edtConfirmPass;
    private Button btnConfirm;
    private ImageView btnBack;
    private FirebaseFirestore db;
    private String currentMssv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
        db = FirebaseFirestore.getInstance();

        // Lấy MSSV từ SharedPreferences (Dùng AppPrefs như bài trước)
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentMssv = prefs.getString("mssv", "");

        // Xử lý sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Xác nhận
        btnConfirm.setOnClickListener(v -> handleChangePassword());
    }

    private void initView() {
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnConfirm = findViewById(R.id.btnConfirmChangePass);
        btnBack = findViewById(R.id.btnBackChangePass);
    }

    private void handleChangePassword() {
        String oldPass = edtOldPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        // 1. Kiểm tra nhập liệu cơ bản
        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentMssv.isEmpty()) {
            Toast.makeText(this, "Lỗi phiên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra mật khẩu cũ trên Firebase
        verifyAndUpdatePassword(oldPass, newPass);
    }

    private void verifyAndUpdatePassword(String inputOldPass, String inputNewPass) {
        // Tìm user theo MSSV
        db.collection("users")
                .whereEqualTo("mssv", currentMssv)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String currentDbPass = document.getString("password");
                        String docId = document.getId(); // Lấy ID tài liệu để update

                        // So sánh mật khẩu cũ nhập vào với mật khẩu trong DB
                        if (currentDbPass != null && currentDbPass.equals(inputOldPass)) {
                            // Mật khẩu cũ ĐÚNG -> Tiến hành cập nhật
                            updatePasswordToFirebase(docId, inputNewPass);
                        } else {
                            Toast.makeText(this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updatePasswordToFirebase(String docId, String newPass) {
        db.collection("users").document(docId)
                .update("password", newPass)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình đổi pass
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}