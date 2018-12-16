package com.example.sarth.smartmirrorapp;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
    private String serialized_data;
    public String status;
    public String status_filter;
    public String everything;
    public byte[] data;

    Poster(Map<String, String> params) {
        String idd = String.valueOf(params.get("id"));
        double d = Double.valueOf(idd);
        int x = (int) d;
        this.id = String.valueOf(x);
        this.title = params.get("title");
        this.category = params.get("category");
        this.name = params.get("contact_name");
        this.number = params.get("contact_number");
        this.email = params.get("contact_email");
        this.postDate = params.get("date_posted");
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

    public static Comparator<Poster> TitleAscending = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            return o1.title.compareTo(o2.title);
        }
    };

    public static Comparator<Poster> TitleDescending = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            return o2.title.compareTo(o1.title);
        }
    };

    public static Comparator<Poster> CategoryAscending = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            return o1.category.compareTo(o2.category);
        }
    };

    public static Comparator<Poster> CategoryDescending = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            return o2.category.compareTo(o1.category);
        }
    };

    public static Comparator<Poster> NameAscending = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            return o1.name.compareTo(o2.name);
        }
    };
    public static Comparator<Poster> NameDescending = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            return o2.name.compareTo(o1.name);
        }
    };

    public static Comparator<Poster> Status = new Comparator<Poster>() {
        @Override
        public int compare(Poster o1, Poster o2) {
            HashMap<String,Integer> poster_priority = new HashMap<>();
            poster_priority.put("expired",4);
            poster_priority.put("rejected",3);
            poster_priority.put("posted",2);
            poster_priority.put("approved",1);
            poster_priority.put("pending",0);
            return poster_priority.get(o1.status).compareTo(poster_priority.get(o2.status));
        }
    };





}
