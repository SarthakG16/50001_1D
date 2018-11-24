package com.example.sarth.smartmirrorapp;

import android.content.Intent;
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

public class AdminActivity extends AppCompatActivity {

    private final static String TAG = "Logcat";

    private SwipeRefreshLayout refreshLayout;

    private Button button_request;
    private Button button_approve;
    private Button button_search;

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
        button_approve = findViewById(R.id.ApprovedButton);
        button_search = findViewById(R.id.SearchButton);

        text_request= findViewById(R.id.RequestNumberView);
        text_approve = findViewById(R.id.ApprovedNumberView);
        text_search= findViewById(R.id.SearchNumberView);

        //text_request.setText("You have " + " pending posters.");
        //text_search.setText("There are currently " + " posters.");



        button_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AdminToRequest = new Intent(AdminActivity.this, RequestsActivity.class);
                Log.i(TAG,"Moving to Request page");
                startActivity(AdminToRequest);
            }
        });

        button_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Toast.makeText(AdminActivity.this,"To make a new activity",Toast.LENGTH_LONG).show();
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
            Intent Logout = new Intent(AdminActivity.this,MainActivity.class);
            Log.i(TAG,"Moving to Login page");
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(Logout);
        }

        return super.onOptionsItemSelected(item);
    }

}