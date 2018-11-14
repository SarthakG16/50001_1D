package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    private ListView filter_posterslist;
    private ArrayAdapter filter_search_adapter;
    EditText filter_search;

    private Spinner filter_spinner;
    private String filter;

    private final static String KEY = "filter key";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //
        Intent intent = getIntent();
        String def = "title";
        String filter = intent.getStringExtra(KEY);

        //if (filter.equals("title") {}

        //search posters
        //make list of posters
        Poster[] posters_pull = {};
        List<Poster> posters = new ArrayList<>((Arrays.asList(posters_pull)));
        //adaptor and onclicklistener for search bar
        filter_search_adapter = new ArrayAdapter(this,R.layout.activity_filter,posters);
        filter_posterslist = (ListView) findViewById(R.id.filter_posterslist);
        filter_posterslist.setAdapter(filter_search_adapter);

        filter_search = (EditText) findViewById(R.id.filter_search);
        filter_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (FilterActivity.this).filter_search_adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //for spinner
        //make filters
        String[] filters_make = {"Select your Filter","Expiry Date","Location","Title"};
        List<String> filters =  new ArrayList<>(Arrays.asList(filters_make));
        //adapter and onitemselectlistener for spinner
        ArrayAdapter<String> filter_filter_adapter = new ArrayAdapter<>(FilterActivity.this,android.R.layout.simple_spinner_item,filters);
        filter_filter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_spinner = (Spinner)findViewById(R.id.filter_spinner);
        filter_spinner.setAdapter(filter_filter_adapter);

        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //toggle to different posters shown
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




    }
}
