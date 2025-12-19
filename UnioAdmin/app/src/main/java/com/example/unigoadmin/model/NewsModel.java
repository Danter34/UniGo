package com.example.unigoadmin.model;

import java.util.Date;

public class NewsModel {
    private String content;
    private String imageUrl;
    private Date timestamp;
    private String link;

    // THÊM TRƯỜNG NÀY ĐỂ XỬ LÝ XÓA
    private String documentId;

    public NewsModel() { }

    public NewsModel(String content, String imageUrl, Date timestamp, String link) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.link = link;
    }

    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public Date getTimestamp() { return timestamp; }
    public String getLink() { return link; }

    // Getter & Setter cho ID
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}