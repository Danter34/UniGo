package com.example.project_unigo.adapter;

import android.content.Intent;
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
import com.example.project_unigo.model.BoardingHouseModel;
import com.example.project_unigo.view.MapDetailActivity; // Activity bản đồ sắp tạo

import java.util.List;

public class BoardingHouseAdapter extends RecyclerView.Adapter<BoardingHouseAdapter.HouseViewHolder> {

    private List<BoardingHouseModel> mList;

    public BoardingHouseAdapter(List<BoardingHouseModel> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_boarding_house, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        BoardingHouseModel house = mList.get(position);
        if (house == null) return;

        holder.tvName.setText(house.getName());

        Glide.with(holder.itemView.getContext())
                .load(house.getImageUrl())
                .placeholder(R.drawable.loading)
                .into(holder.imgHouse);

        // --- XỬ LÝ CLICK: CHUYỂN SANG TRANG BẢN ĐỒ IN-APP ---
        holder.itemView.setOnClickListener(v -> {
            if (house.getLatitude() != null && house.getLongitude() != null) {
                Intent intent = new Intent(v.getContext(), MapDetailActivity.class);

                // Truyền dữ liệu sang trang bản đồ
                intent.putExtra("name", house.getName());
                intent.putExtra("lat", house.getLatitude()); // kiểu Double
                intent.putExtra("lng", house.getLongitude()); // kiểu Double

                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(v.getContext(), "Chưa có tọa độ bản đồ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class HouseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHouse;
        TextView tvName;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Đảm bảo ID khớp với item_boarding_house.xml
            imgHouse = itemView.findViewById(R.id.imgHouse);
            tvName = itemView.findViewById(R.id.tvHouseName);
        }
    }
}