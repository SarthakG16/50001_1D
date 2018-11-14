package com.example.sarth.smartmirrorapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Poster implements Serializable{
    /*public String description;
    public String category;
    public String name;
    public String number;
    public String email;
    public String postDate;
    public String expiryDate;
    public List<String> locations;
    public String enquiries;*/
    public Uri poster_uri;

/*
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
*/

    public Poster(Uri poster_uri) {
        poster_uri =poster_uri;
    }

}
