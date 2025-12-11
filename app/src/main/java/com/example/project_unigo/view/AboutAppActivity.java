package com.example.project_unigo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        // Nút Back
        findViewById(R.id.btnBackAbout).setOnClickListener(v -> finish());

        // Ánh xạ các nút
        LinearLayout btnFacebook = findViewById(R.id.btnContactFacebook);
        LinearLayout btnZalo = findViewById(R.id.btnContactZalo);
        LinearLayout btnEmail = findViewById(R.id.btnContactEmail);
        LinearLayout btnPrivacy = findViewById(R.id.btnPrivacyPolicy);

        // 1. Xử lý mở link Facebook
        btnFacebook.setOnClickListener(v -> openLink("https://www.facebook.com/truongdaihocyersin"));

        // 2. Xử lý mở Zalo (Ví dụ mở link zalo.me)
        btnZalo.setOnClickListener(v -> openLink("https://zalo.me/your_zalo_id"));

        // 3. Xử lý gửi Email
        btnEmail.setOnClickListener(v -> sendEmail());

        // 4. XỬ LÝ POPUP CHÍNH SÁCH BẢO MẬT (QUAN TRỌNG)
        btnPrivacy.setOnClickListener(v -> showPrivacyDialog());
    }

    // Hàm mở trình duyệt web
    private void openLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Không tìm thấy ứng dụng để mở liên kết", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm mở ứng dụng gửi mail
    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:admin@yersin.edu.vn")); // Thay email của bạn
        intent.putExtra(Intent.EXTRA_SUBJECT, "Góp ý về ứng dụng UniGo");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Không tìm thấy ứng dụng Email", Toast.LENGTH_SHORT).show();
        }
    }

    // --- HÀM HIỂN THỊ POPUP (DIALOG) ---
    private void showPrivacyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Tiêu đề
        builder.setTitle("Thông tin pháp lý & Dữ liệu");

        // Nội dung thông báo (đúng ý bạn yêu cầu)
        String message = "Ứng dụng Yersin UniGo được phát triển nhằm mục đích học tập và nghiên cứu trong khuôn khổ đồ án môn học.\n\n" +
                "1. Dữ liệu sử dụng:\n" +
                "Toàn bộ dữ liệu về ngành học, quy chế và  được thu thập từ các nguồn công khai của trường Đại học Yersin Đà Lạt.\n\n" +
                "2. Một số mục như thông tin nhà trọ là dữ liệu giả được thêm vào để nhằm mục làm đích chức năng mở rộng của ứng dụng.\n\n" +
                "3. Mục đích:\n" +
                "Ứng dụng hoàn toàn phi thương mại (Non-commercial). Chúng tôi không thu thập dữ liệu cá nhân của người dùng cho mục đích quảng cáo.\n\n" +
                "Xin cảm ơn!";

        builder.setMessage(message);

        // Nút xác nhận
        builder.setPositiveButton("Đã hiểu", (dialog, which) -> {
            dialog.dismiss(); // Đóng popup
        });

        // Tạo và hiển thị
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}