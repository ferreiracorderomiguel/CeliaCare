package com.mfc.celiacare.model;

import java.io.Serializable;

public class Places implements Serializable {
    private String name;
    private String streetAddress;
    private String city;
    private String description;
    private String image;
    private String phoneNumber;
    private String date;

    public Places() {
    }

    public Places(String placeName, String streetAddress, String city, String description, String image, String phoneNumber, String date) {
        this.name = placeName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.description = description;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String placeName) {
        this.name = placeName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Places{" +
                "name='" + name + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}