package com.example.project_unigo.model;

import java.io.Serializable; // Bắt buộc để truyền object qua Intent

public class MajorModel implements Serializable {
    private String name;
    private String imageUrl; // Ảnh thumbnail ở danh sách

    // Các trường chi tiết cho trang Detail
    private String bannerUrl;  // Ảnh banner đầu trang
    private String content1;   // Giới thiệu chung
    private String content2;   // Mục tiêu đào tạo
    private String imageUrl1;  // Ảnh minh họa 1
    private String content3;   // Triển vọng nghề nghiệp
    private String imageUrl2;  // Ảnh minh họa 2

    public MajorModel() { }

    // Constructor đầy đủ
    public MajorModel(String name, String imageUrl, String bannerUrl, String content1, String content2, String imageUrl1, String content3, String imageUrl2) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.bannerUrl = bannerUrl;
        this.content1 = content1;
        this.content2 = content2;
        this.imageUrl1 = imageUrl1;
        this.content3 = content3;
        this.imageUrl2 = imageUrl2;
    }

    // Getter
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getBannerUrl() { return bannerUrl; }
    public String getContent1() { return content1; }
    public String getContent2() { return content2; }
    public String getImageUrl1() { return imageUrl1; }
    public String getContent3() { return content3; }
    public String getImageUrl2() { return imageUrl2; }
}