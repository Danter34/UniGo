package com.example.unigoadmin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unigoadmin.Adapter.AdminStudentAdapter;
import com.example.unigoadmin.R;
import com.example.unigoadmin.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminStudentManagerActivity extends AppCompatActivity {

    private EditText edtSearch;
    private Spinner spnSearchType, spnMajor, spnBatch;
    private RecyclerView rcvStudents;
    private FloatingActionButton fabAdd;
    private AdminStudentAdapter adapter;
    private List<UserModel> listUsers;
    private List<String> listMajors;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_manager);

        db = FirebaseFirestore.getInstance();
        initView();
        setupSpinners();
        loadData(); // Load mặc định
    }

    private void initView() {
        edtSearch = findViewById(R.id.edtSearchAdmin);
        spnSearchType = findViewById(R.id.spnSearchType);
        spnMajor = findViewById(R.id.spnMajorFilter);
        spnBatch = findViewById(R.id.spnBatchFilter);
        rcvStudents = findViewById(R.id.rcvAdminStudents);
        fabAdd = findViewById(R.id.fabAddStudent);

        listUsers = new ArrayList<>();
        adapter = new AdminStudentAdapter(this, listUsers, db); // Adapter nhận DB để xử lý reset pass
        rcvStudents.setLayoutManager(new LinearLayoutManager(this));
        rcvStudents.setAdapter(adapter);

        // Sự kiện tìm kiếm
        findViewById(R.id.btnSearchAction).setOnClickListener(v -> filterData());

        // Sự kiện thêm sinh viên
        fabAdd.setOnClickListener(v -> showAddStudentDialog());
    }

    private void setupSpinners() {
        // 1. Loại tìm kiếm
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Mã SV", "Tên"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchType.setAdapter(typeAdapter);

        // 2. Khóa (Batch)
        String[] batches = {"Tất cả", "18", "19", "20", "21", "22"};
        ArrayAdapter<String> batchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, batches);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBatch.setAdapter(batchAdapter);

        // 3. Ngành (Lấy từ Firestore)
        listMajors = new ArrayList<>();
        listMajors.add("Tất cả");
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listMajors);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMajor.setAdapter(majorAdapter);

        db.collection("Majors").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                // Giả sử MajorModel có field 'name'
                String majorName = doc.getString("name");
                if (majorName != null) listMajors.add(majorName);
            }
            majorAdapter.notifyDataSetChanged();
        });
    }

    private void filterData() {
        String searchText = edtSearch.getText().toString().trim();
        String searchType = spnSearchType.getSelectedItem().toString(); // "Mã SV" hoặc "Tên"
        String selectedMajor = spnMajor.getSelectedItem().toString();
        String selectedBatch = spnBatch.getSelectedItem().toString();

        Query query = db.collection("users").whereEqualTo("role", "sinhvien");

        // Lọc theo Ngành
        if (!selectedMajor.equals("Tất cả")) {
            query = query.whereEqualTo("department", selectedMajor);
        }

        // Lọc theo Khóa
        if (!selectedBatch.equals("Tất cả")) {
            query = query.whereEqualTo("batch", selectedBatch);
        }

        // Thực thi query
        query.get().addOnSuccessListener(snapshots -> {
            listUsers.clear();
            for (QueryDocumentSnapshot doc : snapshots) {
                UserModel user = doc.toObject(UserModel.class);

                // Lọc thủ công Search Text (vì Firestore không hỗ trợ nhiều điều kiện contains cùng lúc tốt)
                boolean isMatch = true;
                if (!searchText.isEmpty()) {
                    if (searchType.equals("Mã SV")) {
                        isMatch = user.getMssv().toLowerCase().contains(searchText.toLowerCase());
                    } else {
                        isMatch = user.getFullName().toLowerCase().contains(searchText.toLowerCase());
                    }
                }

                if (isMatch) {
                    listUsers.add(user);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void loadData() {
        // Load mặc định tất cả sinh viên
        db.collection("users").whereEqualTo("role", "sinhvien").get()
                .addOnSuccessListener(snapshots -> {
                    listUsers.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        listUsers.add(doc.toObject(UserModel.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null);
        builder.setView(view);

        // Ánh xạ đúng từ view của dialog
        EditText edtNewMssv = view.findViewById(R.id.edtNewMssv);
        EditText edtNewName = view.findViewById(R.id.edtNewName);
        Spinner spnNewDept = view.findViewById(R.id.spnNewDept);
        EditText edtNewEmail = view.findViewById(R.id.edtNewEmail);
        EditText edtNewPhone = view.findViewById(R.id.edtNewPhone);
        EditText edtNewAddress = view.findViewById(R.id.edtNewAddress);
        Spinner spnNewGender = view.findViewById(R.id.spnNewGender);
        List<String> genders = new ArrayList<>();
        genders.add("Nam");
        genders.add("Nữ");

        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNewGender.setAdapter(genderAdapter);
        // Mở DatePicker
        EditText edtDob = view.findViewById(R.id.edtNewDob);
        setupDobPicker(edtDob);

        // Load danh sách Khoa
        List<String> listDept = new ArrayList<>();
        ArrayAdapter<String> deptAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDept);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNewDept.setAdapter(deptAdapter);

        db.collection("Majors").get().addOnSuccessListener(snap -> {
            listDept.clear();
            for (QueryDocumentSnapshot d : snap) {
                String name = d.getString("name");
                if (name != null) listDept.add(name);
            }
            deptAdapter.notifyDataSetChanged();
        });

        EditText edtNewBatch = view.findViewById(R.id.edtNewBatch);

        Button btnSave = view.findViewById(R.id.btnSaveStudent);
        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String mssv = edtNewMssv.getText().toString().trim();
            String name = edtNewName.getText().toString().trim();
            String dept = spnNewDept.getSelectedItem().toString();
            String batch = edtNewBatch.getText().toString().trim();
            String email = edtNewEmail.getText().toString().trim();
            String phone = edtNewPhone.getText().toString().trim();
            String address = edtNewAddress.getText().toString().trim();
            String gender = spnNewGender.getSelectedItem().toString();
            String dob = edtDob.getText().toString().trim();  // ✔ FIX

            if (mssv.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Cần nhập ít nhất MSSV và Tên", Toast.LENGTH_SHORT).show();
                return;
            }

            UserModel newUser = new UserModel(
                    mssv, mssv, name, "sinhvien", dept, batch, email, phone, address, gender, dob
            );

            db.collection("users").document(mssv).set(newUser)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadData();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        dialog.show();
    }
    private void setupDobPicker(EditText edtDob) {

        // Tắt bàn phím để không focus nhầm
        edtDob.setInputType(android.text.InputType.TYPE_NULL);
        edtDob.setFocusable(false);

        View.OnClickListener openPicker = v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AdminStudentManagerActivity.this,
                    (view, y, m, d) -> {
                        String date = String.format("%02d/%02d/%04d", d, m + 1, y);
                        edtDob.setText(date);
                    },
                    year, month, day
            );
            dialog.show();
        };

        edtDob.setOnClickListener(openPicker);
        edtDob.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) openPicker.onClick(v);
        });
    }
    public void showEditStudentDialog(UserModel user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null);
        builder.setView(view);

        // Ánh xạ view
        EditText edtMssv = view.findViewById(R.id.edtNewMssv);
        EditText edtName = view.findViewById(R.id.edtNewName);
        Spinner spnDept = view.findViewById(R.id.spnNewDept);
        EditText edtBatch = view.findViewById(R.id.edtNewBatch);
        EditText edtEmail = view.findViewById(R.id.edtNewEmail);
        EditText edtPhone = view.findViewById(R.id.edtNewPhone);
        EditText edtAddress = view.findViewById(R.id.edtNewAddress);
        Spinner spnGender = view.findViewById(R.id.spnNewGender);
        EditText edtDob = view.findViewById(R.id.edtNewDob);
        Button btnSave = view.findViewById(R.id.btnSaveStudent);

        // Chỉnh tiêu đề dialog
        TextView tvTitle = view.findViewById(R.id.tvtsv);
        tvTitle.setText("Chỉnh Sửa Sinh Viên");

        // Gán dữ liệu cũ
        edtMssv.setText(user.getMssv());
        edtName.setText(user.getFullName());
        edtBatch.setText(user.getBatch());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());
        edtAddress.setText(user.getAddress());
        edtDob.setText(user.getDob());

        // Không cho sửa MSSV + Họ tên
        edtMssv.setEnabled(false);
        edtName.setEnabled(false);

        // Spinner Giới tính
        List<String> genders = new ArrayList<>();
        genders.add("Nam");
        genders.add("Nữ");

        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(genderAdapter);

        // Chọn đúng giới tính theo dữ liệu
        int genderIndex = genders.indexOf(user.getGender());
        if (genderIndex >= 0) spnGender.setSelection(genderIndex);

        // DatePicker
        setupDobPicker(edtDob);

        // LOAD KHOA từ Firestore
        List<String> listDept = new ArrayList<>();
        ArrayAdapter<String> deptAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDept);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDept.setAdapter(deptAdapter);

        db.collection("Majors").get().addOnSuccessListener(snap -> {
            listDept.clear();
            for (QueryDocumentSnapshot d : snap) {
                String name = d.getString("name");
                if (name != null) listDept.add(name);
            }
            deptAdapter.notifyDataSetChanged();

            // Set đúng ngành
            int deptIndex = listDept.indexOf(user.getDepartment());
            if (deptIndex >= 0) spnDept.setSelection(deptIndex);
        });

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {

            String dept = spnDept.getSelectedItem().toString();
            String batch = edtBatch.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String gender = spnGender.getSelectedItem().toString();
            String dob = edtDob.getText().toString().trim();

            // Cập nhật vào Firestore
            db.collection("users").document(user.getMssv())
                    .update(
                            "department", dept,
                            "batch", batch,
                            "email", email,
                            "phone", phone,
                            "address", address,
                            "gender", gender,
                            "dob", dob
                    )
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadData(); // Refresh danh sách
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        dialog.show();
    }
}