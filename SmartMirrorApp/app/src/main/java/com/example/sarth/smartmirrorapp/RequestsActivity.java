package com.example.sarth.smartmirrorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {
    public static final String TAG =  "Logcat";

    //vars
    private RecyclerView requests;
    private static List<Poster> posters = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private Button search_options_button;

    private CharSequence[] options = {"Title", "Category", "Name"};
    private String choice = options[0].toString();
    private int selected = 0;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Log.i(TAG,"Reach Request Activity");

        search_options_button = findViewById(R.id.requests_search_button);
        requests = findViewById(R.id.request_recycler);

        HashMap<String, String> params = new HashMap<>();

        getPosters(params);

        recyclerViewAdapter = new RecyclerViewAdapter(RequestsActivity.this, posters);
        requests.setAdapter(recyclerViewAdapter);
        requests.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));

        search_options_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_options_button.isActivated()) {
                    return;
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RequestsActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                mBuilder.setTitle("Search Settings");
                mBuilder.setIcon(R.drawable.list_icon);
                mBuilder.setSingleChoiceItems(options, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                        choice = options[which].toString();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
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
                if (choice.equals("Title")) {
                    recyclerViewAdapter.getFilter().filter(newText);
                    return false;
                } else if (choice.equals("Category")) {
                    recyclerViewAdapter.getCategoryFilter().filter(newText);
                    return false;
                } else if (choice.equals("Name")) {
                    recyclerViewAdapter.getNameFilter().filter(newText);
                }
                recyclerViewAdapter.getNameFilter().filter(newText);
                return false;
            }
        });


        return true;
    }

    public void getPosters (HashMap params) {
        Request req = new Request("GET","posters", params, new Request.PostersCallback() {
            @Override
            public void onResponse(List<Poster> posters) {
                Toast.makeText(RequestsActivity.this, "Received posters: " + posters.toString(), Toast.LENGTH_LONG).show();
                RequestsActivity.posters = posters;
            }
        });
        req.execute();
    }
}
