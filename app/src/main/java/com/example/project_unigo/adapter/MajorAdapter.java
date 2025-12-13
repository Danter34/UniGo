package com.example.project_unigo.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_unigo.R;
import com.example.project_unigo.model.MajorModel;
import com.example.project_unigo.view.MajorDetailActivity;

import java.util.List;

public class MajorAdapter extends RecyclerView.Adapter<MajorAdapter.MajorViewHolder> {

    private List<MajorModel> mList;

    public MajorAdapter(List<MajorModel> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MajorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_major, parent, false);
        return new MajorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MajorViewHolder holder, int position) {
        MajorModel major = mList.get(position);

        holder.tvName.setText(major.getName());

        Glide.with(holder.itemView.getContext())
                .load(major.getImageUrl())
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.imgMajor);

        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent chuyển sang trang chi tiết
            Intent intent = new Intent(v.getContext(), MajorDetailActivity.class);

            // Đóng gói object major và gửi đi (Model phải implements Serializable)
            intent.putExtra("major_data", major);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MajorViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMajor;
        TextView tvName;

        public MajorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMajor = itemView.findViewById(R.id.imgMajor);
            tvName = itemView.findViewById(R.id.tvMajorName);
        }
    }
}