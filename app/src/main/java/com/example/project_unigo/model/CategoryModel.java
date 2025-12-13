package com.example.project_unigo.model;

public class CategoryModel {
    private int id;
    private String name;
    private int iconResId;

    public CategoryModel(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
}
