package com.example.project_unigo.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.project_unigo.R;
import com.example.project_unigo.model.MajorModel;

public class MajorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_detail);

        // Nút back
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

        // Ánh xạ views
        ImageView imgBanner = findViewById(R.id.imgDetailBanner);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvContent1 = findViewById(R.id.tvContent1);
        TextView tvContent2 = findViewById(R.id.tvContent2);
        ImageView img1 = findViewById(R.id.imgDetail1);
        TextView tvContent3 = findViewById(R.id.tvContent3);
        ImageView img2 = findViewById(R.id.imgDetail2);

        // Lấy dữ liệu từ Intent
        MajorModel major = (MajorModel) getIntent().getSerializableExtra("major_data");

        if (major != null) {
            // Set Text
            tvName.setText(major.getName() + " là gì?");
            tvContent1.setText(major.getContent1());
            tvContent2.setText(major.getContent2());
            tvContent3.setText(major.getContent3());

            // Set Images dùng Glide
            loadImage(imgBanner, major.getBannerUrl());
            loadImage(img1, major.getImageUrl1());
            loadImage(img2, major.getImageUrl2());
        }
    }

    // Hàm hỗ trợ load ảnh cho gọn code
    private void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.loading) // Thay bằng ảnh loading của bạn
                    .error(R.drawable.loading)
                    .into(view);
        }
    }
}