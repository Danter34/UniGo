package com.example.project_unigo.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.project_unigo.R;
import com.example.project_unigo.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // ID kênh thông báo (Dùng ID mới để tránh xung đột cấu hình cũ)
    private static final String CHANNEL_ID = "UniGo_Students_V1";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Code Admin mới gửi dữ liệu vào "data", nên ta check "data"
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            // Lấy dữ liệu khớp với key bên Admin
            String title = data.get("title");
            String body = data.get("body");
            String soundName = data.get("sound"); // Lấy tên file nhạc

            showNotification(title, body, soundName);
        }

        // Fallback: Nếu Admin lỡ gửi dạng Notification thường
        if (remoteMessage.getNotification() != null) {
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),
                    null
            );
        }
    }

    private void showNotification(String title, String body, String soundName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // PendingIntent (Yêu cầu FLAG_IMMUTABLE trên Android 12+)
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // 1. Xử lý Âm thanh
        Uri soundUri;
        try {
            if (soundName != null && !soundName.isEmpty()) {
                // Tìm ID file nhạc trong res/raw theo tên
                int soundResId = getResources().getIdentifier(soundName, "raw", getPackageName());
                if (soundResId != 0) {
                    soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + soundResId);
                } else {
                    // Nếu không tìm thấy file tên đó, dùng mặc định
                    soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.sound);
                }
            } else {
                // Mặc định
                soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.sound);
            }
        } catch (Exception e) {
            // Phòng hờ lỗi, dùng âm thanh mặc định của hệ thống
            soundUri = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Tạo Channel (Bắt buộc cho Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Nếu Channel cũ đã tồn tại mà không có tiếng, xóa nó đi để tạo lại
            // notificationManager.deleteNotificationChannel(CHANNEL_ID);

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Thông báo Sinh viên", // Tên hiển thị trong Cài đặt
                    NotificationManager.IMPORTANCE_HIGH // QUAN TRỌNG: HIGH để hiện Popup
            );

            // Gắn âm thanh vào Channel
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, audioAttributes);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }

        // 3. Xây dựng Thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) // Thay bằng icon trong suốt (small_icon) nếu có
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Popup
                .setVibrate(new long[]{0, 500, 200, 500}) // Rung: Nghỉ 0ms, Rung 500ms...
                .setContentIntent(pendingIntent);

        // Hiển thị (Dùng System.currentTimeMillis để ID không trùng nhau)
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}