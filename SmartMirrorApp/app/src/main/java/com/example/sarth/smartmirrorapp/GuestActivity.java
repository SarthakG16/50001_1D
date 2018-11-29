package com.example.sarth.smartmirrorapp;

import android.content.Intent;
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

public class GuestActivity extends AppCompatActivity {

    private final static String TAG = "Logcat";

    private Button button_request;
    private Button button_display;
    private Button button_search;
    private FloatingActionButton button_upload;

    private TextView text_request;
    private TextView text_display;
    private TextView text_search;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        refreshLayout = findViewById(R.id.guest_refreshlayout);

        button_request = findViewById(R.id.GuestRequestButton);
        button_display = findViewById(R.id.GuestDisplayingButton);
        button_search = findViewById(R.id.GuestArchiveButton);
        button_upload = findViewById(R.id.GuestUploadButton);

        text_request= findViewById(R.id.GuestRequestNumberView);
        text_display = findViewById(R.id.GuestDisplayingNumberView);
        text_search= findViewById(R.id.GuestArchiveNumberView);

        getData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });


        button_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GuestToRequest = new Intent(GuestActivity.this, AdminFilterActivity.class);
                Log.i(TAG,"Moving to Request page");
                startActivity(GuestToRequest);
            }
        });

        button_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(GuestActivity.this,"To make a new activity",Toast.LENGTH_LONG).show();
                /*
                Intent GuestToDisplaying= new Intent(GuestActivity.this,Displaying.class);
                Log.i(TAG,"Moving to Displaying page");
                startActivity(GuestToDisplaying);
                 */
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GuestToFilter = new Intent(GuestActivity.this,SearchActivity.class);
                Log.i(TAG,"Moving to Search page");
                startActivity(GuestToFilter);
            }
        });


        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GuestToUpload = new Intent(GuestActivity.this,UploadActivity.class);
                Log.i(TAG,"Moving to upload page");
                startActivity(GuestToUpload);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout){
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


                text_request.setText(String.valueOf((int)(d1+d2)));
                text_display.setText(String.valueOf((int)d3));
                text_search.setText(String.valueOf((int)d4));

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
}
