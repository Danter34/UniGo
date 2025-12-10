package com.example.project_unigo.model;

public class BoardingHouseModel {
    private String name;
    private String imageUrl;
    private Double latitude;
    private Double longitude;

    // Constructor rỗng bắt buộc cho Firestore
    public BoardingHouseModel() {
    }

    public BoardingHouseModel(String name, String imageUrl, Double latitude, Double longitude) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}