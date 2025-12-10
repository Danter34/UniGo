package com.example.project_unigo.model;

import java.io.Serializable;

public class RuleModel implements Serializable {
    private String title;
    private String summary;
    private String link;

    public RuleModel() {}

    public RuleModel(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getLink() { return link; }
}
