package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

    private Button button_admin;
    private Button button_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button_admin = findViewById(R.id.toAdmin);
        button_guest = findViewById(R.id.toGuest);

        button_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginToAdmin = new Intent(MainActivity.this,AdminActivity.class);
                Log.i(TAG,"Moving to Admin page");
                startActivity(loginToAdmin);
            }
        });

        button_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginToGuest= new Intent(MainActivity.this,GuestActivity.class);
                Log.i(TAG,"Moving to Guest page");
                startActivity(loginToGuest);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();

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
}