package com.example.sarth.smartmirrorapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Logcat";

    private SwipeRefreshLayout refreshLayout;

    private Button button_request;
    private Button button_display;
    private Button button_search;
    private FloatingActionButton button_upload;

    private TextView text_request;
    private TextView text_approve;
    private TextView text_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        Drawable gradient = getResources().getDrawable( R.drawable.action_bar_gradient);
        bar.setBackgroundDrawable(gradient);

        Intent toAdmin = getIntent();
        setTitle("Welcome Back " + toAdmin.getStringExtra(MainActivity.USER_KEY));

        //set up buttons
        button_request = findViewById(R.id.RequestButton);
        button_display = findViewById(R.id.DisplayingButton);
        button_search = findViewById(R.id.SearchButton);
        button_upload = findViewById(R.id.AdminUploadButton);

        //set up onClickListener
        button_request.setOnClickListener(this);
        button_display.setOnClickListener(this);
        button_search.setOnClickListener(this);
        button_upload.setOnClickListener(this);

        text_request= findViewById(R.id.RequestNumberView);
        text_approve = findViewById(R.id.DisplayingNumberView);
        text_search= findViewById(R.id.SearchNumberView);

        refreshLayout = findViewById(R.id.admin_refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout){
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.myPosters) {
            Intent toArchive = new Intent(AdminActivity.this, GuestFilterActivity.class);
            toArchive.putExtra(AdminFilterActivity.FILTER_KEY,"admin");
            startActivity(toArchive);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        HashMap<String, String> params = new HashMap<>();

        Request req = new Request("GET","posters/status", params, new Request.Callback () {
            @Override
            public void onResponse(String response) {
                Gson g  = new Gson();
                Map<String, String> dataMap = g.fromJson(response, Map.class);
                String temp_pending = String.valueOf(dataMap.get("pending"));
                double d1 = Double.valueOf(temp_pending);
                if (d1>1) {
                    text_request.setTextColor(Color.RED);
                }

                String temp_approved = String.valueOf(dataMap.get("approved"));
                double d2 = Double.valueOf(temp_approved);

                String temp_posted = String.valueOf(dataMap.get("posted"));
                double d3 = Double.valueOf(temp_posted);

                text_request.setText(String.valueOf((int)d1));
                text_approve.setText(String.valueOf((int)d3));
                text_search.setText(String.valueOf((int)(d1+d2+d3)));

                refreshLayout.setRefreshing(false);
                Toast.makeText(AdminActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
            }
        });
        req.execute();

    }

    protected void logout(){
        final HashMap<String, String> params = new HashMap<>();
        Request req_admin = new Request("GET", "auth/logout", params, new Request.Callback() {
            @Override
            public void onResponse(String response) {
                Gson g1  = new Gson();
                final Map<String, String> logout_info1 = g1.fromJson(response, Map.class);
                if (logout_info1.get("status").equals("success")) {
                    Toast.makeText(AdminActivity.this, "Logout success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        req_admin.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Destroying admin page");
        logout();
    }

    @Override
    public void onClick(View v) {
        Intent fromAdmin = null;
        switch(v.getId()){
            case R.id.RequestButton:
                fromAdmin = new Intent(this, AdminFilterActivity.class);
                fromAdmin.putExtra(AdminFilterActivity.FILTER_KEY,"pending");
                Log.i(TAG,"Admin to Request page");
                break;

            case R.id.DisplayingButton:
                fromAdmin = new Intent(this, AdminFilterActivity.class);
                fromAdmin.putExtra(AdminFilterActivity.FILTER_KEY,"posted");
                Log.i(TAG,"Admin to Now Displaying page");
                break;

            case R.id.SearchButton:
                fromAdmin= new Intent(this,SearchActivity.class);
                Log.i(TAG,"Moving to Search page");
                break;

            case R.id.AdminUploadButton:
                fromAdmin= new Intent(this,UploadActivity.class);
                Log.i(TAG,"Admin to Admin Upload page");
                break;
        }
        startActivity(fromAdmin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        getData();
    }
}
