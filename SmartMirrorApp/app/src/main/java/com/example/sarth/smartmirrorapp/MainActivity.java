package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.util.List;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "Logcat";

    private Button button_upload;
    private Button button_filter;
    private Button button_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        button_upload = findViewById(R.id.button_upload);
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToUpload = new Intent(MainActivity.this,UploadActivity.class);
                Log.i(TAG,"Moving to upload page");
                startActivity(mainToUpload);
            }
        });

        button_filter = findViewById(R.id.button_filter);
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToFilter = new Intent(MainActivity.this,FilterActivity.class);
                Log.i(TAG,"Moving to filter page");
                startActivity(mainToFilter);
            }
        });

        button_request = findViewById(R.id.button_request);
        button_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToRequest = new Intent(MainActivity.this, RequestsActivity.class);
                Log.i(TAG,"Moving to Filter");
                startActivity(mainToRequest);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                HashMap<String, String> params = new HashMap<>();
                params.put("username", "admin1");
                params.put("password", "hahaha");
                params.put("requested_privelage", "administrator");
                Request req = new Request("POST","auth/login", params, new Request.Callback () {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                   }
                });
                /*Request req = new Request("GET","posters", params, new Request.PostersCallback() {
                    @Override
                    public void onResponse(List<Poster> posters) {
                        Toast.makeText(MainActivity.this, "Received posters: " + posters.toString(), Toast.LENGTH_LONG).show();
                    }
                });*/
                req.execute();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
