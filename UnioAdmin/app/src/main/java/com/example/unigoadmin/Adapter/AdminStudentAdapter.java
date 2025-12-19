package com.example.unigoadmin.Adapter;

import android.content.Context;
import android.app.AlertDialog; // Hoặc dùng androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unigoadmin.AdminStudentManagerActivity;
import com.example.unigoadmin.R;
import com.example.unigoadmin.model.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminStudentAdapter extends RecyclerView.Adapter<AdminStudentAdapter.StudentViewHolder> {

    private Context context;
    private List<UserModel> mList;
    private FirebaseFirestore db;

    public AdminStudentAdapter(Context context, List<UserModel> mList, FirebaseFirestore db) {
        this.context = context;
        this.mList = mList;
        this.db = db;
    }

    // 1. PHẦN BỊ THIẾU: onCreateViewHolder (Tạo giao diện cho từng dòng)
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_student, parent, false);
        return new StudentViewHolder(view);
    }

    // 2. Xử lý dữ liệu
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        UserModel user = mList.get(position);
        if (user == null) return;

        holder.tvName.setText(user.getFullName());
        holder.tvMssv.setText("MSSV: " + user.getMssv());

        // Click vào item để xem chi tiết
        holder.itemView.setOnClickListener(v -> showDetailDialog(user));

        // Click icon Reset Password
        holder.btnResetPass.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Reset Mật khẩu")
                    .setMessage("Đặt lại mật khẩu cho " + user.getMssv() + " về mặc định (giống MSSV)?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        // Update password = mssv
                        db.collection("users").document(user.getMssv())
                                .update("password", user.getMssv())
                                .addOnSuccessListener(a -> Toast.makeText(context, "Đã reset mật khẩu thành công", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Click icon Edit (Hiện thông báo hoặc mở dialog sửa)
        holder.btnEdit.setOnClickListener(v -> {
            ((AdminStudentManagerActivity) context).showEditStudentDialog(user);
        });

    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    private void showDetailDialog(UserModel user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông tin chi tiết");

        String info = "Họ tên: " + user.getFullName() + "\n" +
                "MSSV: " + user.getMssv() + "\n" +
                "Khoa: " + user.getDepartment() + "\n" +
                "Khóa: " + user.getBatch() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "SĐT: " + user.getPhone() + "\n" +
                "Địa chỉ: " + user.getAddress();

        builder.setMessage(info);
        builder.setPositiveButton("Đóng", null);
        builder.show();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvMssv;
        ImageView btnResetPass, btnEdit;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvMssv = itemView.findViewById(R.id.tvStudentMssv);
            btnResetPass = itemView.findViewById(R.id.btnResetPass);
            btnEdit = itemView.findViewById(R.id.btnEditStudent);
        }
    }
}