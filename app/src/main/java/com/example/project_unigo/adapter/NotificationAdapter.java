package com.example.project_unigo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.project_unigo.R;
import com.example.project_unigo.model.NotificationModel;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {

    private List<NotificationModel> mListNoti;

    // Hàm khởi tạo nhận danh sách dữ liệu
    public NotificationAdapter(List<NotificationModel> mListNoti) {
        this.mListNoti = mListNoti;
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout item_notification_row vào
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_row, parent, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        // Lấy dữ liệu tại vị trí position và gán lên giao diện
        NotificationModel noti = mListNoti.get(position);
        if (noti == null) return;

        holder.tvTitle.setText(noti.getTitle());
        holder.tvDate.setText(noti.getDate());
    }

    @Override
    public int getItemCount() {
        if (mListNoti != null) return mListNoti.size();
        return 0;
    }

    // Class nắm giữ các View (TextView, ImageView...)
    public class NotiViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotiTitle);
            tvDate = itemView.findViewById(R.id.tvNotiDate);
        }
    }
}
