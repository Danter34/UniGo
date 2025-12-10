package com.example.project_unigo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_unigo.R;
import com.example.project_unigo.model.RuleModel;

public class RuleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_detail);

        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvSummary = findViewById(R.id.tvDetailContent);
        TextView tvLink = findViewById(R.id.tvDetailLink);

        RuleModel rule = (RuleModel) getIntent().getSerializableExtra("rule_data");

        if (rule != null) {
            tvTitle.setText(rule.getTitle());
            tvSummary.setText(rule.getSummary());

            // Không hiện full link
            tvLink.setText("Sinh viên xem đầy đủ tại đây");
            tvLink.setTextColor(getResources().getColor(R.color.blue));
            tvLink.setPaintFlags(tvLink.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);

            // Mở link bằng Google Chrome
            tvLink.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(rule.getLink()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        }
    }
}
