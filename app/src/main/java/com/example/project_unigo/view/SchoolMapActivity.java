package com.example.project_unigo.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;

public class SchoolMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_map);

        ImageView btnBack = findViewById(R.id.btnBackMap);
        Button btnDirections = findViewById(R.id.btnDirections);

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút Chỉ đường
        btnDirections.setOnClickListener(v -> {
            // Chuyển sang màn hình Google Maps tích hợp bên trong app
            Intent intent = new Intent(SchoolMapActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }
}