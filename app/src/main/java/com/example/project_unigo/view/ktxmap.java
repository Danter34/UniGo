package com.example.project_unigo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.project_unigo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ktxmap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Lấy SupportMapFragment và báo khi bản đồ sẵn sàng
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng yersinUni = new LatLng(11.984883402394301, 108.4261699347103);

        // 2. Thêm Marker (Ghim đỏ) vào bản đồ
        mMap.addMarker(new MarkerOptions().position(yersinUni).title("Ký túc xá"));

        // 3. Di chuyển camera tới trường và zoom vào (mức zoom 15)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yersinUni, 15f));

        // 4. Bật nút hiển thị vị trí của tôi (Chấm xanh)
        enableMyLocation();
    }

    private void enableMyLocation() {
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Nếu chưa có quyền, xin quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Nếu đã có quyền, bật layer vị trí
        mMap.setMyLocationEnabled(true);
    }
}