package com.example.project_unigo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_unigo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String houseName;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);

        // 1. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        houseName = intent.getStringExtra("name");
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);

        // 2. Xử lý nút Back và Title
        findViewById(R.id.btnBackMap).setOnClickListener(v -> finish());
        TextView tvTitle = findViewById(R.id.tvMapTitle);
        if (houseName != null) {
            tvTitle.setText(houseName);
        }

        // 3. Khởi tạo Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Tạo đối tượng tọa độ
        LatLng location = new LatLng(lat, lng);

        // Thêm Marker (cái ghim đỏ) vào bản đồ
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(houseName)); // Tiêu đề khi ấn vào ghim

        // Di chuyển camera đến vị trí đó và zoom vào (mức zoom 15)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f));

        // Bật các nút điều khiển zoom (+/-)
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}