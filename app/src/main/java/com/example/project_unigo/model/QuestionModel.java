package com.example.project_unigo.model;

import java.util.Date;

public class QuestionModel {
    private String mssv;
    private String topic;
    private String content;
    private Date timestamp;
    private String status;

    public QuestionModel() { }

    public QuestionModel(String mssv, String topic, String content, Date timestamp, String status) {
        this.mssv = mssv;
        this.topic = topic;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getMssv() { return mssv; }
    public String getTopic() { return topic; }
    public String getContent() { return content; }
    public Date getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
}