package com.example.project_unigo.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;

public class SchoolInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);

        // Nút Back
        findViewById(R.id.btnBackInfo).setOnClickListener(v -> finish());
        setupVideo();
        // Setup dữ liệu cho 4 ô Giá trị (Thay R.drawable.xxx bằng icon thật của bạn)
        setupBox(R.id.box_grad_3y, "Tốt nghiệp 3 năm", "Tiết kiệm thời gian, sớm gia nhập thị trường.", R.drawable.student);
        setupBox(R.id.box_business, "Học kỳ doanh nghiệp", "Trải nghiệm thực tế tại doanh nghiệp đối tác.", R.drawable.idea);
        setupBox(R.id.box_global, "Công dân toàn cầu", "Đào tạo kỹ năng, ngoại ngữ chuẩn quốc tế.", R.drawable.cdtc);
        setupBox(R.id.box_happy, "Trường học hạnh phúc", "Môi trường năng động, tôn trọng và yêu thương.", R.drawable.book);
    }
    private void setupVideo() {
        VideoView videoView = findViewById(R.id.videoIntro);

        // SỬA ĐỔI: Sử dụng Uri.parse và setVideoURI thay vì setVideoPath
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ys);
        videoView.setVideoURI(uri);

        // Auto play
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);  // Lặp vô hạn

            // Chỉnh âm lượng (0f, 0f là tắt tiếng, 1f, 1f là max volume)
            // Nếu muốn có tiếng thì bỏ dòng này hoặc set là 1f, 1f
            mp.setVolume(0f, 0f);

            videoView.start();
        });

        // Thêm dòng này để VideoView nhận focus (quan trọng trên một số máy)
        videoView.requestFocus();
    }
    private void setupBox(int includeId, String title, String desc, int iconRes) {
        View view = findViewById(includeId);
        if (view != null) {
            TextView tvTitle = view.findViewById(R.id.tvValueTitle);
            TextView tvDesc = view.findViewById(R.id.tvValueDesc);
            ImageView img = view.findViewById(R.id.imgValueIcon);

            tvTitle.setText(title);
            tvDesc.setText(desc);
            img.setImageResource(iconRes);
        }
    }
}