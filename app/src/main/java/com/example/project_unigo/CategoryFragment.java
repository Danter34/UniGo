package com.example.project_unigo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        setupItem(view, R.id.cat_info_school, "Thông tin trường", R.drawable.ttt);
        setupItem(view, R.id.cat_info_dept, "Thông tin khoa", R.drawable.ttk);
        setupItem(view, R.id.cat_rules, "Thông tin nội quy", R.drawable.ttnq);

        setupItem(view, R.id.cat_map, "Bản đồ trường", R.drawable.bdt);
        setupItem(view, R.id.cat_housing, "Tìm kiếm nhà trọ", R.drawable.tknt);
        setupItem(view, R.id.cat_dorm, "Ký túc xá", R.drawable.ktx);

        setupItem(view, R.id.cat_faq, "Hỏi đáp", R.drawable.hd);
        setupItem(view, R.id.cat_training, "Chương trình đào tạo", R.drawable.ctdt);
        setupItem(view, R.id.cat_student_info, "Thông tin sinh viên", R.drawable.ttsv);

        return view;
    }

    private void setupItem(View parentView, int includeId, String rawTitle, int iconRes) {
        LinearLayout itemLayout = parentView.findViewById(includeId);

        if (itemLayout != null) {
            ImageView img = itemLayout.findViewById(R.id.imgCategory);
            TextView tv = itemLayout.findViewById(R.id.tvCategoryName);

            // Xử lý text xuống dòng
            tv.setText(formatTitle(rawTitle));

            // Set icon
            img.setImageResource(iconRes);

            // Xử lý click
            itemLayout.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Chọn: " + rawTitle, Toast.LENGTH_SHORT).show();
            });
        }
    }

    private String formatTitle(String text) {
        if (text == null || text.isEmpty()) return "";
        String[] words = text.trim().split("\\s+");

        // Nếu chỉ có 1 hoặc 2 từ thì hiển thị trên 1 dòng
        if (words.length <= 2) return text;

        StringBuilder builder = new StringBuilder();
        // Ghép 2 từ đầu tiên rồi xuống dòng
        builder.append(words[0]).append(" ").append(words[1]).append("\n");

        // Ghép các từ còn lại vào dòng dưới
        for (int i = 2; i < words.length; i++) {
            builder.append(words[i]);
            if (i < words.length - 1) builder.append(" ");
        }
        return builder.toString();
    }
}