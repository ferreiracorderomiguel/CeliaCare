package com.mfc.celiacare.model;

public class News {
    private String title;
    private String description;
    private String image;
    private String date;
    private String timeSinceUpdated;

    public News() {
    }

    public News(String title, String description, String image, String date) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public News(String title, String description, String image, String date, String timeSinceUpdated) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
        this.timeSinceUpdated = timeSinceUpdated;
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

    @Override
    public String toString() {
        return "NewsModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}