package com.example.project_unigo.model;

public class UserModel {
    private String mssv;      // Dùng làm ID đăng nhập
    private String password;  // Mật khẩu
    private String fullName;
    private String role;      // Ví dụ: "sinhvien", "giangvien", "admin"
    private String department; // Khoa
    private String batch;     // Khóa (VD: K15, K16)
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String dob;       // Ngày tháng năm sinh

    // Bắt buộc phải có Constructor rỗng cho Firestore
    public UserModel() { }

    public UserModel(String mssv, String password, String fullName, String role, String department, String batch, String email, String phone, String address, String gender, String dob) {
        this.mssv = mssv;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.department = department;
        this.batch = batch;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
    }

    // Getter methods (Firestore cần getter để đọc dữ liệu)
    public String getMssv() { return mssv; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getBatch() { return batch; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
}