package com.example.sarth.smartmirrorapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {
    public static final String TAG =  "Logcat";

    //vars
    private ArrayList<Poster> posters = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Log.i(TAG,"Reach Request Actiity");

        posters = new ArrayList<>(Poster.requests);
        RecyclerView requests = findViewById(R.id.request_recycler);
        recyclerViewAdapter = new RecyclerViewAdapter(RequestsActivity.this, posters);
        requests.setAdapter(recyclerViewAdapter);
        requests.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_requests_search, menu);

        MenuItem searchItem = menu.findItem(R.id.requests_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }
}
