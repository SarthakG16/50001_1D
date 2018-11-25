package com.example.sarth.smartmirrorapp;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poster implements Serializable{
    public String id;
    public String title;
    public String category;
    public String name;
    public String number;
    public String email;
    public String postDate;
    public String expiryDate;
    public String locations;
    public String serialized_data;
    public String status;
    public String status_filter;
    public String everything;
    public byte[] data;

    //public static HashMap<String,Poster> archive = new HashMap<>();
    public static List<Poster> requests = new ArrayList<>();




    public Poster(Map<String, String> params) {
        this.id = String.valueOf(params.get("id"));
        this.title = params.get("title");
        this.category = params.get("category");
        this.name = params.get("contact_name");
        this.number = params.get("contact_number");
        this.email = params.get("contact_email");
        this.postDate = params.get("date_posted");
        Log.i("REQ_", String.valueOf(this.postDate==null));
        this.expiryDate = params.get("date_expiry");
        this.locations = params.get("locations");
        this.serialized_data = params.get("serialized_image_data");
        this.data = Base64.decode(serialized_data);
        this.status = params.get("status");
        if(this.status.equals("posted")) {
            this.status_filter = "On Display";
        } else {
            this.status_filter = this.status;
        }
        this.everything = this.title + " " + this.category + " " + this.name + " " + this.status_filter;
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
                this.locations, this.serialized_data);
    }
}
