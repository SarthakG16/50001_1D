package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class AdminActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshLayout = findViewById(R.id.admin_refreshlayout);

        button_request= findViewById(R.id.RequestButton);
        button_display = findViewById(R.id.DisplayingButton);
        button_search = findViewById(R.id.SearchButton);
        button_upload = findViewById(R.id.AdminUploadButton);

        text_request= findViewById(R.id.RequestNumberView);
        text_approve = findViewById(R.id.DisplayingNumberView);
        text_search= findViewById(R.id.SearchNumberView);

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
                Intent AdminToRequest = new Intent(AdminActivity.this, RequestsActivity.class);
                Log.i(TAG,"Moving to Request page");
                startActivity(AdminToRequest);
            }
        });

        button_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(AdminActivity.this,"To make a new activity",Toast.LENGTH_LONG).show();
                /*
                Intent AdminToDisplaying= new Intent(AdminActivity.this,Displaying.class);
                Log.i(TAG,"Moving to Displaying page");
                startActivity(AdminToDisplaying);
                 */
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AdminToSearch= new Intent(AdminActivity.this,SearchActivity.class);
                Log.i(TAG,"Moving to Search page");
                startActivity(AdminToSearch);
            }
        });

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AdminToUpload= new Intent(AdminActivity.this,UploadActivity.class);
                Log.i(TAG,"Moving to Upload page");
                startActivity(AdminToUpload);

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
            //TODO: remove user details
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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
                text_approve.setText(String.valueOf((int)d2));
                text_search.setText(String.valueOf((int)d3));

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
        Log.i(TAG,"Destroying Admin page");
        logout();
    }


}
