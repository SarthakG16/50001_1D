package com.example.sarth.smartmirrorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuestFilterActivity extends AppCompatActivity {

    public static final String TAG =  "Logcat";

    //vars
    private RecyclerView requests;
    private static List<Poster> posters = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private Button search_options_button;
    private SwipeRefreshLayout refreshLayout;

    private CharSequence[] options = {"Title", "Category", "Name"};
    private String choice = "";
    private int selected = -1;

    private ProgressBar progressBar;

    public static final String FILTER_KEY = "Filter_key";
    private String filter = "pending";



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent toRequest = getIntent();
        filter = toRequest.getStringExtra(FILTER_KEY);
        Log.i("REQ_",filter);

        switch (filter) {
            case "pending":
                setTitle("Your Requests");
                break;
            case "posted":
                setTitle("On Display");
                break;
        }

        search_options_button = findViewById(R.id.requests_search_button);
        requests = findViewById(R.id.request_recycler);
        refreshLayout = findViewById(R.id.refresh_layout);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getPosters();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosters();
            }
        });


        search_options_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_options_button.isActivated()) {
                    return;
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GuestFilterActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
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
                    recyclerViewAdapter.getTitleFilter().filter(newText);
                    return false;
                } else if (choice.equals("Category")) {
                    recyclerViewAdapter.getCategoryFilter().filter(newText);
                    return false;
                } else if (choice.equals("Name")) {
                    recyclerViewAdapter.getNameFilter().filter(newText);
                } else {
                    recyclerViewAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });


        return true;
    }

    public void getPosters () {
        HashMap<String, String> params = new HashMap<>();


        Request req = new Request("GET","posters/mine", params, new Request.PostersCallback() {
            @Override
            public void onResponse(List<Poster> posters) {
                GuestFilterActivity.posters = posters;
                Poster.requests = posters;

                recyclerViewAdapter = new RecyclerViewAdapter(GuestFilterActivity.this, posters,"Request");
                requests.setAdapter(recyclerViewAdapter);
                requests.setLayoutManager(new LinearLayoutManager(GuestFilterActivity.this));

                progressBar.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }
        });
        req.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
