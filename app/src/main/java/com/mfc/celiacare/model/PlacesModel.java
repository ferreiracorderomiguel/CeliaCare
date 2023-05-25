package com.mfc.celiacare.model;

import android.media.Image;

public class PlacesModel {
    private String placeName;
    private String streetAddress;
    private String city;
    private String description;
    private Integer image;
    private String phoneNumber;

    public PlacesModel() {
    }

    public PlacesModel(String placeName, String streetAddress, String city, String description, Integer image, String phoneNumber) {
        this.placeName = placeName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.description = description;
        this.image = image;
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
