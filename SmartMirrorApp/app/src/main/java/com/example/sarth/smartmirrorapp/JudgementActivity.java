package com.example.sarth.smartmirrorapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class JudgementActivity extends AppCompatActivity{
    private final static String TAG = "Logcat";
    private TextView title;

    private PDFView pdfView;
    private Button poster_preview_button;

    private TextView category;
    private TextView start_date;
    private TextView stop_date;

    private TextView contact_name;
    private TextView contact_number;
    private TextView contact_email;

    private CheckBox[] locations;

    private LinearLayout button_layout;
    private Button button_reject;
    private Button button_approve;
    private Button button_remove;

    private String origin;
    private Poster poster;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final int position = intent.getIntExtra("Position",0);
        poster = RecyclerViewAdapter.posterList.get(position);
        setTitle(poster.title);

        origin = intent.getStringExtra("Origin");

        button_layout = findViewById(R.id.judge_button_layout);
        button_reject = findViewById(R.id.judge_reject);
        button_approve = findViewById(R.id.judge_approve);
        button_remove = findViewById(R.id.judge_remove_search);

        if (!poster.status.equals("pending")) {
            button_layout.setVisibility(View.INVISIBLE);
            button_remove.setVisibility(View.VISIBLE);
        }

        pdfView = findViewById(R.id.judge_pdfView);
        pdfView.fromBytes(poster.data).load();

        title = findViewById(R.id.judge_title);
        title.setText(String.format("Title:     %s", poster.title));

        category = findViewById(R.id.judge_categories_title);
        category.setText(String.format("Category:     %s", poster.category));

        contact_name = findViewById(R.id.judge_contact_name);
        contact_name.setText(String.format("Name: %s", poster.name));

        contact_number = findViewById(R.id.judge_contact_number);
        contact_number.setText(String.format("Number: %s", poster.number));

        contact_email = findViewById(R.id.judge_contact_email);
        contact_email.setText(String.format("Email: %s", poster.email));

        start_date = findViewById(R.id.judge_date_start);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(poster.postDate));
        } catch (ParseException e) {
            Toast.makeText(JudgementActivity.this,"Wrong Date Shown",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        start_date.setText(String.format("Start Date: %s", DateFormat.getDateInstance().format(cal.getTime())));

        stop_date = findViewById(R.id.judge_date_stop);
        try {
            cal.setTime(sdf.parse(poster.expiryDate));
        } catch (ParseException e) {
            Toast.makeText(JudgementActivity.this,"Wrong Date Shown",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        stop_date.setText(String.format("Stop Date: %s", DateFormat.getDateInstance().format(cal.getTime())));

        locations = new CheckBox[8];
        for (int i = 0; i < locations.length; i++) {
            int resID = getResources().getIdentifier(String.format("judge_building_%s", i + 1), "id", getPackageName());
            locations[i] = findViewById(resID);
            if (poster.locations.contains(locations[i].getText().toString())) {
                locations[i].setChecked(true);
            }
            locations[i].setClickable(false); // Not allowing the checkbox to be clickable so that Admin may not click by mistake
        }

        poster_preview_button = findViewById(R.id.judge_poster_preview_button);

        poster_preview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Button Clicked");
                if (poster.data == null) {
                    return;
                }
                AlertDialog.Builder mBuilder =  new AlertDialog.Builder(JudgementActivity.this,android.R.style.ThemeOverlay_Material_Dialog);
                mBuilder.setTitle(poster.title);
                View poster_layout = getLayoutInflater().inflate(R.layout.dialog_poster,null);
                PDFView poster_expanded = poster_layout.findViewById(R.id.poster_expanded);
                poster_expanded.fromBytes(poster.data).load();;
                mBuilder.setView(poster_layout);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    public void remove(View view) {
        Toast.makeText(JudgementActivity.this,"Removed",Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(JudgementActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
        builder.setTitle("Confirm");
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setMessage("Delete the Poster: '" + poster.title+"' ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                if (poster.status.equals("posted")) {
                    params.put("status","pending"); //TODO change to expired
                } else {
                    params.put("status", "pending"); //TODO change later to rejected
                }
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JudgementActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,response);
                        Intent toRequests = new Intent(JudgementActivity.this,AdminFilterActivity.class);
                        toRequests.putExtra(AdminFilterActivity.FILTER_KEY, poster.status);
                        startActivity(toRequests);
                    }
                });
                req.execute();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reject(View view) {
        Toast.makeText(JudgementActivity.this,"Rejected",Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(JudgementActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
        builder.setTitle("Confirm");
        builder.setIcon(R.drawable.ic_close_black_24dp);
        builder.setMessage("Reject the Poster: '" + poster.title+"' ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                params.put("status","rejected");
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JudgementActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,response);
                        Intent toRequests = new Intent(JudgementActivity.this,AdminFilterActivity.class);
                        toRequests.putExtra(AdminFilterActivity.FILTER_KEY, poster.status);
                        startActivity(toRequests);
                    }
                });
                req.execute();


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void approve(View view) {

        Toast.makeText(JudgementActivity.this,"Approved",Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(JudgementActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
        builder.setTitle("Confirm");
        builder.setIcon(R.drawable.ic_check_black_24dp);
        builder.setMessage("Approve the Poster: '" + poster.title+"' ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                params.put("status","posted");
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JudgementActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,response);
                        Intent toRequests = new Intent(JudgementActivity.this,AdminFilterActivity.class);
                        toRequests.putExtra(AdminFilterActivity.FILTER_KEY, poster.status);
                        startActivity(toRequests);
                    }
                });
                req.execute();
                finish();


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
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
}
