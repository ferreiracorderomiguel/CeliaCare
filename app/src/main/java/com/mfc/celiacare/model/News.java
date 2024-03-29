package com.mfc.celiacare.model;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String description;
    private String image;
    private String date;
    private String timeSinceUpdated;
    private String source;

    public News() {
    }

    public News(String title, String description, String image, String date, String source) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
        this.source = source;
    }

    public News(String title, String description, String image, String date, String source, String timeSinceUpdated) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
        this.timeSinceUpdated = timeSinceUpdated;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSinceUpdated() {
        return timeSinceUpdated;
    }

    public void setTimeSinceUpdated(String timeSinceUpdated) {
        this.timeSinceUpdated = timeSinceUpdated;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}