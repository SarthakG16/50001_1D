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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GuestFilterActivity extends AppCompatActivity {

    public static final String TAG = "Logcat";
    public static final String FILTER_KEY = "Filter_key";
    //vars
    private RecyclerView requests;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout refreshLayout;
    private CharSequence[] search_options = {"Title", "Category", "Name"};
    private String search_choice = "";
    private int search_selected = -1;
    private CharSequence[] sort_options = {"Title(A-Z)", "Title(Z-A)", "Category(A-Z)", "Category(Z-A)", "Name(A-Z)", "Name(Z-A)", "Status"};
    private String sort_choice = "";
    private int sort_selected = -1;
    private String filter = "pending";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Log.i(TAG, "Create");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent toRequest = getIntent();
        filter = toRequest.getStringExtra(FILTER_KEY);

        switch (filter) {
            case "&status=pending,approved,rejected":
                setTitle("Your Requests");
                break;
            case "&status=posted":
                setTitle("On Display");
                break;
            case "&status=expired":
                setTitle("Archive");
                break;
            case "My Posters":
                setTitle("My Posters");
                filter="";
                break;
        }

        requests = findViewById(R.id.request_recycler);
        refreshLayout = findViewById(R.id.refresh_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosters();
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
                if (recyclerViewAdapter == null) {
                    return false;
                }
                switch (search_choice) {
                    case "Title":
                        recyclerViewAdapter.getTitleFilter().filter(newText);
                        return false;
                    case "Category":
                        recyclerViewAdapter.getCategoryFilter().filter(newText);
                        return false;
                    case "Name":
                        recyclerViewAdapter.getNameFilter().filter(newText);
                        break;
                    default:
                        recyclerViewAdapter.getFilter().filter(newText);
                        break;
                }
                return false;
            }
        });

        return true;
    }

    public void getPosters() {
        HashMap<String, String> params = new HashMap<>();

        Request req = new Request("GET", "posters/filter?mine=1" + filter, params, new Request.PostersCallback() {
            @Override
            public void onResponse(List<Poster> posters) {

                switch (sort_choice) {
                    case "Title(A-Z)":
                        Collections.sort(posters, Poster.TitleAscending);
                        break;
                    case "Title(Z-A)":
                        Collections.sort(posters, Poster.TitleDescending);
                        break;
                    case "Category(A-Z)":
                        Collections.sort(posters, Poster.CategoryAscending);
                        break;
                    case "Category(Z-A)":
                        Collections.sort(posters, Poster.CategoryDescending);
                        break;
                    case "Name(A-Z)":
                        Collections.sort(posters, Poster.NameAscending);
                        break;
                    case "Name(Z-A)":
                        Collections.sort(posters, Poster.NameDescending);
                    case "Status":
                        Collections.sort(posters, Poster.Status);
                    default:
                        break;
                }

                Poster.posters = posters;

                recyclerViewAdapter = new RecyclerViewAdapter(GuestFilterActivity.this, posters, "Guest");
                requests.setAdapter(recyclerViewAdapter);
                requests.setLayoutManager(new LinearLayoutManager(GuestFilterActivity.this));

                refreshLayout.setRefreshing(false);
            }
        });
        req.execute();
    }

    public void search_options(MenuItem menuItem) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GuestFilterActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        mBuilder.setTitle("Search Settings");
        mBuilder.setIcon(R.drawable.list_icon);
        mBuilder.setSingleChoiceItems(search_options, search_selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                search_selected = which;
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<CharSequence> temp = Arrays.asList(search_options);
                search_selected = temp.indexOf(search_choice);
            }
        });
        mBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (search_selected == -1) {
                    return;
                }
                search_choice = search_options[search_selected].toString();
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public void sort(MenuItem menuItem) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GuestFilterActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        mBuilder.setTitle("Sort Settings");
        mBuilder.setIcon(R.drawable.sort_icon);
        mBuilder.setSingleChoiceItems(sort_options, sort_selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sort_selected = which;
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<CharSequence> temp = Arrays.asList(sort_options);
                sort_selected = temp.indexOf(sort_choice);

            }
        });
        mBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sort_selected == -1) {
                    return;
                }
                sort_choice = sort_options[sort_selected].toString();
                switch (sort_choice) {
                    case "Title(A-Z)":
                        recyclerViewAdapter.sort(Poster.TitleAscending);
                        break;
                    case "Title(Z-A)":
                        recyclerViewAdapter.sort(Poster.TitleDescending);
                        break;
                    case "Category(A-Z)":
                        recyclerViewAdapter.sort(Poster.CategoryAscending);
                        break;
                    case "Category(Z-A)":
                        recyclerViewAdapter.sort(Poster.CategoryDescending);
                        break;
                    case "Name(A-Z)":
                        recyclerViewAdapter.sort(Poster.NameAscending);
                        break;
                    case "Name(Z-A)":
                        recyclerViewAdapter.sort(Poster.NameDescending);
                        break;
                    case "Status":
                        recyclerViewAdapter.sort(Poster.Status);
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
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

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        getPosters();
    }
}
