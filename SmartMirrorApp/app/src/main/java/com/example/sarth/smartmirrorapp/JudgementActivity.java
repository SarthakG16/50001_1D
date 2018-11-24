package com.example.sarth.smartmirrorapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.HashMap;

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

        Intent intent = getIntent();

        final int position = intent.getIntExtra("Position",0);
        poster = Poster.requests.get(position);
        setTitle(poster.title);
        Toast.makeText(JudgementActivity.this,poster.id,Toast.LENGTH_LONG).show();

        origin = intent.getStringExtra("Origin");

        button_layout = findViewById(R.id.judge_button_layout);
        button_reject = findViewById(R.id.judge_reject);
        button_approve = findViewById(R.id.judge_approve);
        button_remove = findViewById(R.id.judge_remove_search);

        if (origin.equals("Search")) {
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
        start_date.setText(String.format("Start Date: %s", poster.postDate));

        stop_date = findViewById(R.id.judge_date_stop);
        stop_date.setText(String.format("Stop Date: %s", poster.expiryDate));

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
                /*HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                params.put("status","posted");
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JudgementActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,response);
                    }
                });
                req.execute();*/
                Intent toRequests = new Intent(JudgementActivity.this,RequestsActivity.class);
                startActivity(toRequests);

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
                /*HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                params.put("status","rejected");
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JudgementActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,response);
                    }
                });
                req.execute();*/
                Intent toRequests = new Intent(JudgementActivity.this,RequestsActivity.class);
                startActivity(toRequests);

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
                /*HashMap<String,String> params = new HashMap<>();
                params.put("id",poster.id);
                params.put("status","posted");
                Request req = new Request("POST","posters/", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(JudgementActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,response);
                    }
                });
                req.execute();*/
                Intent toRequests = new Intent(JudgementActivity.this,RequestsActivity.class);
                startActivity(toRequests);

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
}
