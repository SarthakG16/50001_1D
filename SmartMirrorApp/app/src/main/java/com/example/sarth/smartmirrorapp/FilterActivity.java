package com.example.sarth.smartmirrorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    private Spinner spinner_filters;
    //private ListView poster_list;
    private ArrayAdapter filter_adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        spinner_filters = findViewById(R.id.spinner_categories);
        String[] filters = {"Select your Filter","Expiry Date","Location","Title"};

        List<String> categories =  new ArrayList<>(Arrays.asList(filters));

        //filter_adapter = new ArrayAdapter(this,R.layout.list_i);
    }
}
