package com.example.project_unigo.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_unigo.adapter.MajorAdapter;
import com.example.project_unigo.R;
import com.example.project_unigo.model.MajorModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TrainingProgramActivity extends AppCompatActivity {

    private RecyclerView rcvMajors;
    private MajorAdapter adapter;
    private List<MajorModel> listMajors;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_program);

        findViewById(R.id.btnBackTraining).setOnClickListener(v -> finish());

        rcvMajors = findViewById(R.id.rcvMajors);
        listMajors = new ArrayList<>();

        adapter = new MajorAdapter(listMajors);
        rcvMajors.setLayoutManager(new GridLayoutManager(this, 2));
        rcvMajors.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadMajorsFromFirebase();
    }

    private void loadMajorsFromFirebase() {
        CollectionReference ref = db.collection("Majors");

        ref.get().addOnSuccessListener(query -> {
            listMajors.clear();

            int limit = 4;
            int count = 0;

            for (var doc : query) {
                if (count >= limit) break; // <= GIỚI HẠN 4 ITEM

                MajorModel major = doc.toObject(MajorModel.class);
                listMajors.add(major);

                count++;
            }

            adapter.notifyDataSetChanged();
        });
    }
}
