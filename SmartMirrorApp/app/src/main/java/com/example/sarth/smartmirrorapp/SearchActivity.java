package com.example.sarth.smartmirrorapp;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG =  "Logcat";

    //vars
    private RecyclerView search_recycler;
    private static List<Poster> posters = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout refreshLayout;

    private CharSequence[] sort_options = {"Title(A-Z)","Title(Z-A)", "Category(A-Z)","Category(Z-A)", "Name(A-Z)","Name(Z-A)"};
    private String sort_choice = "";
    private int sort_selected = -1;

    private CharSequence[] search_options = {"Title", "Category", "Name","Status"};
    private String search_choice = "";
    private int search_selected = -1;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        Drawable gradient = getResources().getDrawable( R.drawable.action_bar_gradient);
        bar.setBackgroundDrawable(gradient);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_recycler = findViewById(R.id.request_recycler);
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
                    case "Status":
                        recyclerViewAdapter.getStatusFilter().filter(newText);
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

    public void getPosters () {
        HashMap<String, String> params = new HashMap<>();
        Request req = new Request("GET","posters", params, new Request.PostersCallback() {
            @Override
            public void onResponse(List<Poster> posters) {
                List<Poster> filteredPosters = new ArrayList<>();
                for (Poster p : posters) {
                    if (!(p.status.equals("expired") || p.status.equals("rejected"))) {
                        filteredPosters.add(p);
                    }
                }
                SearchActivity.posters = filteredPosters;
                Poster.posters = filteredPosters;

                switch (sort_choice) {
                    case "Title(A-Z)":
                        Collections.sort(filteredPosters, Poster.TitleAscending);
                        break;
                    case "Title(Z-A)":
                        Collections.sort(filteredPosters, Poster.TitleDescending);
                        break;
                    case "Category(A-Z)":
                        Collections.sort(filteredPosters, Poster.CategoryAscending);
                        break;
                    case "Category(Z-A)":
                        Collections.sort(filteredPosters, Poster.CategoryDescending);
                        break;
                    case "Name(A-Z)":
                        Collections.sort(filteredPosters, Poster.NameAscending);
                        break;
                    case "Name(Z-A)":
                        Collections.sort(filteredPosters, Poster.NameDescending);
                    default:
                        break;
                }
                recyclerViewAdapter = new RecyclerViewAdapter(SearchActivity.this, filteredPosters, "Admin");
                search_recycler.setAdapter(recyclerViewAdapter);
                search_recycler.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                refreshLayout.setRefreshing(false);
            }
        });
        req.execute();
    }

    public void search_options(MenuItem menuItem) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
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
                if (search_selected == -1){
                    return;
                }
                search_choice = search_options[search_selected].toString();
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public void sort(MenuItem item) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
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
                if (sort_selected == -1){
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
