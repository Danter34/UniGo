package com.example.project_unigo.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project_unigo.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvDepartment, tvBatch;
    private TextView btnAdmin, btnLogout;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 1. Ánh xạ View
        tvName = view.findViewById(R.id.tvProfileName);
        tvDepartment = view.findViewById(R.id.tvProfileDepartment);
        tvBatch = view.findViewById(R.id.tvProfileBatch);
        btnAdmin = view.findViewById(R.id.btnAdmin);
        btnLogout = view.findViewById(R.id.btnLogout);

        db = FirebaseFirestore.getInstance();

        // 2. Tải thông tin User
        loadUserProfile();

        // 3. Xử lý Đăng xuất
        btnLogout.setOnClickListener(v -> handleLogout());

        // 4. Xử lý nút Admin (ví dụ)
        btnAdmin.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Vào trang quản trị", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(getContext(), AdminActivity.class);
            // startActivity(intent);
        });

        return view;
    }

    private void loadUserProfile() {
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String mssv = prefs.getString("mssv", "");

        // 1. KIỂM TRA CACHE TRƯỚC
        String cachedName = prefs.getString("fullName", null);
        String cachedDept = prefs.getString("department", null);
        String cachedBatch = prefs.getString("batch", null);
        String cachedRole = prefs.getString("role", null);

        // Nếu có cache → dùng luôn (SIÊU NHANH)
        if (cachedName != null && cachedDept != null && cachedBatch != null && cachedRole != null) {
            tvName.setText(cachedName);
            tvDepartment.setText(cachedDept);
            tvBatch.setText(cachedBatch);

            btnAdmin.setVisibility("admin".equals(cachedRole) ? View.VISIBLE : View.GONE);
            return; // NGỪNG – KHÔNG GỌI FIREBASE NỮA
        }

        // 2. NẾU CHƯA CÓ CACHE → GỌI FIREBASE (chỉ chạy 1 lần)
        if (!mssv.isEmpty()) {
            db.collection("users")
                    .whereEqualTo("mssv", mssv)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                            String name = document.getString("fullName");
                            String department = document.getString("department");
                            String batch = document.getString("batch");
                            String role = document.getString("role");

                            // Cập nhật UI
                            tvName.setText(name != null ? name : "Chưa cập nhật");
                            tvDepartment.setText(department != null ? department : "---");
                            tvBatch.setText(batch != null ? batch : "---");

                            btnAdmin.setVisibility("admin".equals(role) ? View.VISIBLE : View.GONE);

                            // 3. LƯU CACHE
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("fullName", name);
                            editor.putString("department", department);
                            editor.putString("batch", batch);
                            editor.putString("role", role);
                            editor.apply();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi tải thông tin", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void handleLogout() {
        // 1. Xóa trạng thái đăng nhập trong SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Xóa hết dữ liệu (mssv, isLoggedIn,...)
        editor.apply();

        // 2. Chuyển về màn hình Login
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // Xóa back stack để người dùng không bấm Back quay lại được
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // 3. Đóng Activity hiện tại (MainActivity)
        getActivity().finish();
    }
}