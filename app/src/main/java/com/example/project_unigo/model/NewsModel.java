package com.example.project_unigo.model;

import java.util.Date;

public class NewsModel {
    private String content;
    private String imageUrl;
    private Date timestamp;
    private String link;

    public NewsModel() { } // Constructor rỗng cho Firebase

    public NewsModel(String content, String imageUrl, Date timestamp,String link) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.link = link;
    }

    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public Date getTimestamp() { return timestamp; }
    public String getLink() { return link; }
}
