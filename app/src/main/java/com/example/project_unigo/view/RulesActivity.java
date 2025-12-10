package com.example.project_unigo.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_unigo.R;
import com.example.project_unigo.adapter.RuleAdapter;
import com.example.project_unigo.model.RuleModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RulesActivity extends AppCompatActivity {

    private RecyclerView rcvRules;
    private RuleAdapter adapter;
    private List<RuleModel> listRules;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        findViewById(R.id.btnBackTraining).setOnClickListener(v -> finish());

        rcvRules = findViewById(R.id.rcvRules);
        listRules = new ArrayList<>();

        adapter = new RuleAdapter(listRules);
        rcvRules.setLayoutManager(new LinearLayoutManager(this));
        rcvRules.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadRulesFromFirebase();
    }

    private void loadRulesFromFirebase() {
        CollectionReference ref = db.collection("Rules");

        ref.get().addOnSuccessListener(query -> {
            listRules.clear();

            query.forEach(doc -> {
                RuleModel rule = doc.toObject(RuleModel.class);
                listRules.add(rule);
            });

            adapter.notifyDataSetChanged();
        });
    }
}
