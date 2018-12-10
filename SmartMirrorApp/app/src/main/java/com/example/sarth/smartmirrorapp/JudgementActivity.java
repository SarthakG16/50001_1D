package com.example.sarth.smartmirrorapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
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
import com.google.gson.Gson;

import org.apache.commons.io.output.TaggedOutputStream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

        if (origin.equals("Guest")) {
            if(poster.status.equals("posted")) {
                button_layout.setVisibility(View.GONE);
            } else {
                if (poster.status.equals("pending") || poster.status.equals("approved")) {
                    button_remove.setText("Cancel");
                }
                button_layout.setVisibility(View.INVISIBLE);
                button_remove.setVisibility(View.VISIBLE);
            }
        } else if (origin.equals("Admin") && !poster.status.equals("pending")) {
            if(poster.status.equals("approved")) {
                button_remove.setText("Reject");
            }
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
            locations[i].setTypeface(ResourcesCompat.getFont(JudgementActivity.this,R.font.abel));

        }

        poster_preview_button = findViewById(R.id.judge_poster_preview_button);

        poster_preview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Button Clicked");
                if (poster.data == null) {
                    return;
                }
                AlertDialog.Builder mBuilder =  new AlertDialog.Builder(JudgementActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                mBuilder.setTitle(poster.title);
                View poster_layout = getLayoutInflater().inflate(R.layout.dialog_poster,null);
                PDFView poster_expanded = poster_layout.findViewById(R.id.poster_expanded);
                poster_expanded.fromBytes(poster.data).load();
                mBuilder.setView(poster_layout);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    public void remove(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(JudgementActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
        builder.setTitle("Confirm");
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setMessage("Delete the Poster: '" + poster.title+"' ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                if(origin.equals("Admin")) {
                    if (poster.status.equals("posted")) {
                        params.put("status", "expired");
                    } else {
                        params.put("status", "rejected");
                    }
                    Request req = new Request("POST", "posters/", params, new Request.Callback() {
                        @Override
                        public void onResponse(String response) {
                            Gson g = new Gson();
                            Map<String,String> details = g.fromJson(response, Map.class);
                            if (details.get("status").equals("success")) {
                                Toast.makeText(JudgementActivity.this,"Poster Removed",Toast.LENGTH_LONG).show();
                                finish();
                            } else if (details.get("status").equals("failure")) {
                                Toast.makeText(JudgementActivity.this, details.get("error_message"), Toast.LENGTH_SHORT).show();
                            }
                            Log.i(TAG,response);
                        }
                    });
                    req.execute();
                } else if (origin.equals("Guest")) {
                    if (!poster.status.equals("posted")) {
                        Request req = new Request("POST", "posters/cancel", params, new Request.Callback() {
                            @Override
                            public void onResponse(String response) {
                                Gson g = new Gson();
                                Map<String,String> details = g.fromJson(response, Map.class);
                                if (details.get("status").equals("success")) {
                                    Toast.makeText(JudgementActivity.this,"Poster Removed",Toast.LENGTH_LONG).show();
                                    finish();
                                } else if (details.get("status").equals("failure")) {
                                    Toast.makeText(JudgementActivity.this, details.get("error_message"), Toast.LENGTH_SHORT).show();
                                }
                                Log.i(TAG,response);
                            }
                        });
                        req.execute();
                    }
                }
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
                        Gson g = new Gson();
                        Map<String,String> details = g.fromJson(response, Map.class);
                        if (details.get("status").equals("success")) {
                            Toast.makeText(JudgementActivity.this,"Poster Rejected",Toast.LENGTH_LONG).show();
                            finish();
                        } else if (details.get("status").equals("failure")) {
                            Toast.makeText(JudgementActivity.this, details.get("error_message"), Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG,response);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(JudgementActivity.this,android.R.style.ThemeOverlay_Material_Dialog_Alert);
        builder.setTitle("Confirm");
        builder.setIcon(R.drawable.ic_check_black_24dp);
        builder.setMessage("Approve the Poster: '" + poster.title+"' ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                params.put("status","approved");
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        Map<String,String> details = g.fromJson(response, Map.class);
                        if (details.get("status").equals("success")) {
                            Toast.makeText(JudgementActivity.this,"Poster Approved",Toast.LENGTH_LONG).show();
                            finish();
                        } else if (details.get("status").equals("failure")) {
                            Toast.makeText(JudgementActivity.this, details.get("error_message"), Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG,response);
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
