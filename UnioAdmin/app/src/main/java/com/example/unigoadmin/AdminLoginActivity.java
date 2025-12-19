package com.example.unigoadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unigoadmin.AdminDashboardActivity;
import com.example.unigoadmin.R;
import com.example.unigoadmin.model.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText edtMssv, edtPass;
    private Button btnLogin;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMssv = findViewById(R.id.edtMssv);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String mssv = edtMssv.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();

        if (TextUtils.isEmpty(mssv) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query tìm admin
        db.collection("users")
                .whereEqualTo("mssv", mssv)
                .whereEqualTo("password", pass)
                .whereEqualTo("role", "admin") // Chỉ admin mới vào được
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Lưu session nếu cần
                        Toast.makeText(this, "Đăng nhập Admin thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, AdminDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Sai thông tin hoặc không có quyền Admin", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}