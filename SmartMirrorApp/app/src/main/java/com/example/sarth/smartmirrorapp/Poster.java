package com.example.sarth.smartmirrorapp;

import java.util.List;

public class Poster {
    public String description;
    public String category;
    public String name;
    public String number;
    public String email;
    public String postDate;
    public String expiryDate;
    public List<String> locations;
    public String enquiries;

    public Poster(String description, String category, String name, String number, String email, String postDate, String expiryDate, List<String> locations, String enquiries) {
        this.description = description;
        this.category = category;
        this.name = name;
        this.number = number;
        this.email = email;
        this.postDate = postDate;
        this.expiryDate = expiryDate;
        this.locations = locations;
        this.enquiries = enquiries;
    }

}
