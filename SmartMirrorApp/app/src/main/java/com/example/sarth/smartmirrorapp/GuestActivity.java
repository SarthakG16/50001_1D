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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

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
        button_search = findViewById(R.id.GuestSearchButton);
        button_upload = findViewById(R.id.GuestUploadButton);

        text_request= findViewById(R.id.GuestRequestNumberView);
        text_display = findViewById(R.id.GuestDisplayingButton);
        text_search= findViewById(R.id.GuestSearchNumberView);


        button_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GuestToRequest = new Intent(GuestActivity.this, RequestsActivity.class);
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
            //TODO: remove user details
            Intent intent = new Intent(GuestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            /*
            Intent Logout = new Intent(GuestActivity.this,MainActivity.class);
            Log.i(TAG,"Moving to Login page");
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(Logout);
            */
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Destroying page. To clear login details");
    }
}
