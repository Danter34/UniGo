package com.example.unigoadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unigoadmin.R;
import com.example.unigoadmin.model.NewsModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminNewsAdapter extends RecyclerView.Adapter<AdminNewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsModel> mList;
    private OnDeleteListener deleteListener;

    // Interface để gọi hàm xóa bên Activity
    public interface OnDeleteListener {
        void onDelete(String documentId);
    }

    public AdminNewsAdapter(Context context, List<NewsModel> mList, OnDeleteListener deleteListener) {
        this.context = context;
        this.mList = mList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = mList.get(position);
        if (news == null) return;

        // 1. Gán nội dung
        holder.tvContent.setText(news.getContent());

        // 2. Format ngày tháng
        if (news.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            holder.tvDate.setText("Đăng ngày: " + sdf.format(news.getTimestamp()));
        }

        // 3. Load ảnh bằng Glide
        Glide.with(context)
                .load(news.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // Ảnh chờ
                .error(R.drawable.ic_launcher_background)       // Ảnh lỗi
                .into(holder.imgThumb);

        // 4. Xử lý nút Xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa tin tức")
                    .setMessage("Bạn có chắc chắn muốn xóa tin này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (deleteListener != null && news.getDocumentId() != null) {
                            deleteListener.onDelete(news.getDocumentId());
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb, btnDelete;
        TextView tvContent, tvDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgNewsThumb);
            tvContent = itemView.findViewById(R.id.tvNewsContent);
            tvDate = itemView.findViewById(R.id.tvNewsDate);
            btnDelete = itemView.findViewById(R.id.btnDeleteNews);
        }
    }
}