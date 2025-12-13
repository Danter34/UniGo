package com.example.project_unigo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private List<String> imageUrls;

    public BannerAdapter(List<String> imageUrls) { this.imageUrls = imageUrls; }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        // Dùng phép chia lấy dư để lặp lại vị trí trong list gốc
        int realPosition = position % imageUrls.size();

        Glide.with(holder.itemView.getContext())
                .load(imageUrls.get(realPosition))
                .into((ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        //  Trả về số lượng cực lớn để giả lập vô tận
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(@NonNull View itemView) { super(itemView); }
    }
}