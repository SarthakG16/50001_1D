package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class GuestActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Logcat";

    private Button button_request;
    private Button button_display;
    private Button button_archive;
    private FloatingActionButton button_upload;

    private TextView text_request;
    private TextView text_display;
    private TextView text_archive;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_activity);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        Drawable gradient = getResources().getDrawable( R.drawable.action_bar_gradient);
        bar.setBackgroundDrawable(gradient);

        Intent toGuest = getIntent();
        setTitle("Welcome Back " + toGuest.getStringExtra(MainActivity.USER_KEY));

        //set up buttons
        button_request = findViewById(R.id.GuestRequestButton);
        button_display = findViewById(R.id.GuestDisplayingButton);
        button_archive = findViewById(R.id.GuestArchiveButton);
        button_upload = findViewById(R.id.GuestUploadButton);

        //set up onClickListener
        button_request.setOnClickListener(this);
        button_display.setOnClickListener(this);
        button_archive.setOnClickListener(this);
        button_upload.setOnClickListener(this);

        text_request= findViewById(R.id.GuestRequestNumberView);
        text_display = findViewById(R.id.GuestDisplayingNumberView);
        text_archive= findViewById(R.id.GuestArchiveNumberView);

        //get posters from server
        getData();

        refreshLayout = findViewById(R.id.guest_refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem myPoster = menu.findItem(R.id.myPosters);
        myPoster.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout){
            Intent intent = new Intent(GuestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        HashMap<String, String> params = new HashMap<>();

        Request req = new Request("GET","posters/my_status", params, new Request.Callback () {
            @Override
            public void onResponse(String response) {
                Gson g  = new Gson();
                Map<String, String> dataMap = g.fromJson(response, Map.class);
                String temp_pending = String.valueOf(dataMap.get("pending"));
                double d1 = Double.valueOf(temp_pending);

                String temp_approved = String.valueOf(dataMap.get("approved"));
                double d2 = Double.valueOf(temp_approved);

                String temp_posted = String.valueOf(dataMap.get("posted"));
                double d3 = Double.valueOf(temp_posted);

                String temp_expired = String.valueOf(dataMap.get("expired"));
                double d4 = Double.valueOf(temp_expired);

                String temp_rejected = String.valueOf(dataMap.get("rejected"));
                double d5 = Double.valueOf(temp_rejected);

                text_request.setText(String.valueOf((int)(d1+d2+d5)));
                text_display.setText(String.valueOf((int)d3));
                text_archive.setText(String.valueOf((int)d4));

                refreshLayout.setRefreshing(false);
                Toast.makeText(GuestActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GuestActivity.this, "Logout success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        req_admin.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Destroying Guest page");
        logout();
    }

    @Override
    public void onClick(View v) {
        Intent fromGuest = null;
        switch(v.getId()){
            case R.id.GuestRequestButton:
                fromGuest = new Intent(this, GuestFilterActivity.class);
                fromGuest.putExtra(GuestFilterActivity.FILTER_KEY,"request");
                Log.i(TAG,"Guest to Request page");
                break;

            case R.id.GuestDisplayingButton:
                fromGuest = new Intent(this, GuestFilterActivity.class);
                fromGuest.putExtra(GuestFilterActivity.FILTER_KEY,"display");
                Log.i(TAG,"Guest to Now Displaying page");
                break;

            case R.id.GuestArchiveButton:
                fromGuest= new Intent(this, GuestFilterActivity.class);
                fromGuest.putExtra(GuestFilterActivity.FILTER_KEY,"archive");
                Log.i(TAG,"Guest to Archive page");
                break;

            case R.id.GuestUploadButton:
                fromGuest= new Intent(this,UploadActivity.class);
                Log.i(TAG,"Guest to Guest Upload page");
                break;
        }
        startActivity(fromGuest);
    }
}
