package com.example.project_unigo.model;

public class CategoryModel {
    private int id;          // ID để phân biệt
    private String name;     // Tên danh mục
    private int iconResId;   // ID ảnh trong drawable (ví dụ: R.drawable.ic_shirt)

    public CategoryModel(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}
