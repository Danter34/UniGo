package com.example.project_unigo.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;

public class KtxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ktx_activity);

        ImageView btnBack = findViewById(R.id.btnBackInfo);
        Button btnDirections = findViewById(R.id.btnlocation);

        btnBack.setOnClickListener(v -> finish());

        btnDirections.setOnClickListener(v -> {
            Intent intent = new Intent(KtxActivity.this, ktxmap.class);
            startActivity(intent);
        });
    }
}