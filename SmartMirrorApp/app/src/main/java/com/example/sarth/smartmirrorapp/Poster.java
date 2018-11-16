package com.example.sarth.smartmirrorapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.Map;

public class Poster implements Serializable{
    public String title;
    public String category;
    public String name;
    public String number;
    public String email;
    public String postDate;
    public String expiryDate;
    public String locations;
    public String data;

    public Poster(String title) {
        this.title = title;
    }

    public Poster(String title, String category, String name,
                  String number, String email, String postDate, String expiryDate, String locations,String data) {
        this.title = title;
        this.category = category;
        this.name = name;
        this.number = number;
        this.email = email;
        this.postDate = postDate;
        this.expiryDate = expiryDate;
        this.locations = locations;
        this.data = data;
    }

    public Poster(Map<String, String> params) {
        this.title = params.get("title");
        this.category = params.get("category");
        this.name = params.get("name");
        this.number = params.get("contact_number");
        this.email = params.get("contact_email");
        this.postDate = params.get("date_posted");
        this.expiryDate = params.get("date_expiry");
        this.locations = params.get("locations");
        this.data = params.get("notes_to_admin");
    }

    public void setDataFromBytes(byte[] dataFromBytes) {
        this.data = Base64.encodeToString(dataFromBytes);
    }

    public String toString() {
        return String.format(
                "Title: %s, Category: %s, \n" +
                "ContactName: %s, ContactEmail: %s, ContactNumber: %s, \n" +
                "PostDate: %s, ExpiryDate: %s, " +
                "Locations: %s, Data: %s",
                this.title, this.category,
                this.name, this.email, this.number,
                this.postDate, this.expiryDate,
                this.locations, this.data);
    }
}
