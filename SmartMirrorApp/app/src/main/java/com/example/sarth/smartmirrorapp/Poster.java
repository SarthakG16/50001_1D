package com.example.sarth.smartmirrorapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

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

    public void setDataFromBytes(byte[] dataFromBytes) {
        this.data = Base64.encodeToString(dataFromBytes);
    }
}
