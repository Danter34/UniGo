package com.example.project_unigo.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;

import android.content.SharedPreferences;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText edtMssv, edtPassword;
    private Button btnLogin;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Kiểm tra trạng thái đăng nhập trước khi load giao diện
        checkLoginStatus();

        setContentView(R.layout.login);

        edtMssv = findViewById(R.id.edtMssv);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String mssv = edtMssv.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (mssv.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra trên Firestore
        // Tìm trong collection "users", document nào có field "mssv" == nhập vào và "password" == nhập vào
        db.collection("users")
                .whereEqualTo("mssv", mssv)
                .whereEqualTo("password", pass)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Đăng nhập thành công (Tìm thấy user khớp)
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy thông tin user nếu cần thiết
                            String userName = document.getString("fullName");
                            Toast.makeText(LoginActivity.this, "Xin chào " + userName, Toast.LENGTH_SHORT).show();

                            // 3. Lưu trạng thái đăng nhập vào máy
                            saveLoginState(mssv, userName);

                            // 4. Chuyển sang màn hình chính
                            goToMainActivity();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai MSSV hoặc Mật khẩu!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveLoginState(String mssv, String name) {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true); // Đánh dấu đã đăng nhập
        editor.putString("mssv", mssv);        // Lưu lại MSSV để dùng sau này
        editor.putString("name", name);
        editor.apply();
    }

    private void checkLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Đóng LoginActivity lại để user không back về được
    }
}
