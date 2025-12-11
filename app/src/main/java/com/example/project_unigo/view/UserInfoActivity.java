package com.example.project_unigo.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;
import com.example.project_unigo.model.UserModel; // Đảm bảo đã import Model
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity {

    // Khai báo View
    private TextView tvDisplayName;
    private TextView tvFullName, tvMssv, tvGender, tvDob, tvPhone, tvEmail, tvDepartment, tvBatch, tvAddress;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info); // Đảm bảo layout xml đúng tên

        initView();

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Gọi hàm tải dữ liệu
        loadUserProfile();
    }

    private void initView() {
        // Nút Back
        findViewById(R.id.btnBackInfo).setOnClickListener(v -> finish());

        // Ánh xạ các trường thông tin
        tvDisplayName = findViewById(R.id.tvDisplayName); // Tên to dưới avatar

        tvFullName = findViewById(R.id.tvFullName);
        tvMssv = findViewById(R.id.tvMssv);
        tvGender = findViewById(R.id.tvGender);
        tvDob = findViewById(R.id.tvDob);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvBatch = findViewById(R.id.tvBatch);
        tvAddress = findViewById(R.id.tvAddress);
    }

    private void loadUserProfile() {
        // 1. DÙNG ĐÚNG TÊN FILE PREFS NHƯ BÊN PROFILE FRAGMENT ("AppPrefs")
        SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String mssv = prefs.getString("mssv", "");

        if (mssv.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập hoặc lỗi phiên làm việc", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. QUERY FIREBASE GIỐNG HỆT BÊN PROFILE FRAGMENT
        // (Dùng .whereEqualTo thay vì .document)
        db.collection("users") // Lưu ý: chữ "users" viết thường giống code mẫu bạn đưa
                .whereEqualTo("mssv", mssv)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Lấy document đầu tiên tìm thấy
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        // Convert sang Model (UserModel phải có constructor rỗng và getter/setter)
                        UserModel user = document.toObject(UserModel.class);

                        if (user != null) {
                            fillDataToView(user);
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy thông tin sinh viên này", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fillDataToView(UserModel user) {
        // Hàm hiển thị dữ liệu lên màn hình, check null để tránh lỗi
        tvDisplayName.setText(user.getFullName() != null ? user.getFullName() : "---");

        tvFullName.setText(user.getFullName() != null ? user.getFullName() : "---");
        tvMssv.setText(user.getMssv() != null ? user.getMssv() : "---");
        tvGender.setText(user.getGender() != null ? user.getGender() : "---");
        tvDob.setText(user.getDob() != null ? user.getDob() : "---");
        tvPhone.setText(user.getPhone() != null ? user.getPhone() : "---");
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "---");
        tvDepartment.setText(user.getDepartment() != null ? user.getDepartment() : "---");
        tvBatch.setText(user.getBatch() != null ? user.getBatch() : "---");
        tvAddress.setText(user.getAddress() != null ? user.getAddress() : "---");
    }
}