package com.mfc.celiacare.model;

public class Places {
    private String name;
    private String streetAddress;
    private String city;
    private String description;
    private Integer image;
    private String phoneNumber;

    public Places() {
    }

    public Places(String placeName, String streetAddress, String city, String description, Integer image, String phoneNumber) {
        this.name = placeName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.description = description;
        this.image = image;
        this.phoneNumber = phoneNumber;
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

    @Override
    public String toString() {
        return "PlacesModel{" +
                "name='" + name + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
