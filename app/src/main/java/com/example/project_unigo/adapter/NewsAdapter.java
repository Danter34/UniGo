package com.example.project_unigo.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_unigo.R;
import com.example.project_unigo.model.NewsModel;

import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsModel> list;

    public NewsAdapter(List<NewsModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = list.get(position);

        holder.tvContent.setText(news.getContent());

        // Xử lý thời gian (Time Ago)
        holder.tvTime.setText(getTimeAgo(news.getTimestamp()));

        // Load ảnh bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(news.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // Ảnh chờ
                .into(holder.imgNews);
        holder.itemView.setOnClickListener(v -> {
            String url = news.getLink();
            // Kiểm tra xem có link không
            if (url != null && !url.isEmpty()) {
                try {
                    // Nếu link không bắt đầu bằng http, tự động thêm vào để tránh lỗi
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "https://" + url;
                    }

                    // Tạo Intent mở trình duyệt
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    holder.itemView.getContext().startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(holder.itemView.getContext(), "Không thể mở liên kết này", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(holder.itemView.getContext(), "Tin tức này không có liên kết", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    // Hàm tính thời gian
    private String getTimeAgo(Date date) {
        if (date == null) return "";
        long time = date.getTime();
        long now = System.currentTimeMillis();
        long diff = now - time;

        if (diff < 60 * 1000) return "Vừa xong";
        if (diff < 60 * 60 * 1000) return (diff / (60 * 1000)) + " phút trước";
        if (diff < 24 * 60 * 60 * 1000) return (diff / (60 * 60 * 1000)) + " giờ trước";
        if (diff < 7 * 24 * 60 * 60 * 1000) return (diff / (24 * 60 * 60 * 1000)) + " ngày trước";

        // Nếu lâu quá thì hiện ngày tháng
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("dd/MM/yyyy", date).toString();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvContent;
        ImageView imgNews;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTimeAgo);
            tvContent = itemView.findViewById(R.id.tvContent);
            imgNews = itemView.findViewById(R.id.imgNews);
        }
    }
}
