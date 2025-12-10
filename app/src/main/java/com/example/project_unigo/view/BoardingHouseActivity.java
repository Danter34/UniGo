package com.example.project_unigo.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_unigo.R;
import com.example.project_unigo.adapter.BoardingHouseAdapter;
import com.example.project_unigo.model.BoardingHouseModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BoardingHouseActivity extends AppCompatActivity {

    private RecyclerView rcvHouses;
    private BoardingHouseAdapter adapter;
    private List<BoardingHouseModel> listHouses;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding_house); // Tạo layout này tương tự activity_major_center

        // Nút Back
        findViewById(R.id.btnBackHouse).setOnClickListener(v -> finish());

        // Header Title (Nếu cần set lại text)
        TextView tvHeader = findViewById(R.id.tvHeaderHouse);
        if (tvHeader != null) tvHeader.setText("Nhà trọ gần trường");

        // Init RecyclerView
        rcvHouses = findViewById(R.id.rcvHouses);
        listHouses = new ArrayList<>();

        // Init Adapter
        adapter = new BoardingHouseAdapter(listHouses);
        rcvHouses.setLayoutManager(new LinearLayoutManager(this)); // Dùng LinearLayout cho danh sách dọc
        rcvHouses.setAdapter(adapter);

        // Init Firestore
        db = FirebaseFirestore.getInstance();

        // Load Data
        loadHousesFromFirebase();
    }

    private void loadHousesFromFirebase() {
        // Đảm bảo tên Collection khớp với trên Firebase
        CollectionReference ref = db.collection("BoardingHouses");

        ref.get().addOnSuccessListener(query -> {
            listHouses.clear();
            if (!query.isEmpty()) {
                query.forEach(doc -> {
                    BoardingHouseModel house = doc.toObject(BoardingHouseModel.class);
                    listHouses.add(house);
                });
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Chưa có dữ liệu nhà trọ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}