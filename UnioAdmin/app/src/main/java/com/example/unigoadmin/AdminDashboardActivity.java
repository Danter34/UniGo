package com.example.unigoadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        findViewById(R.id.btnManageStudent).setOnClickListener(v ->
                startActivity(new Intent(this, com.example.unigoadmin.AdminStudentManagerActivity.class)));

        findViewById(R.id.btnManageNews).setOnClickListener(v ->
                startActivity(new Intent(this, com.example.unigoadmin.AdminNewsManagerActivity.class)));
    }
}
