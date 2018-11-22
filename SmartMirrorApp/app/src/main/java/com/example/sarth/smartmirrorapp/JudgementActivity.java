package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

public class JudgementActivity extends AppCompatActivity{

    private TextView title;

    private PDFView pdfView;
    private TextView category;
    private TextView start_date;
    private TextView stop_date;

    private TextView contact_name;
    private TextView contact_number;
    private TextView contact_email;

    private CheckBox[] locations;

    private Poster poster;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgment);

        Intent intent = getIntent();
        int position = intent.getIntExtra("Position",0);

        poster = Poster.requests.get(position);

        pdfView = findViewById(R.id.judge_pdfView);
        pdfView.fromBytes(poster.data).load();

        title = findViewById(R.id.judge_title);
        title.setText(String.format("Title: %s", poster.title));

        category = findViewById(R.id.judge_categories_title);
        category.setText(String.format("Category: %s", poster.category));

        contact_name = findViewById(R.id.judge_contact_name);
        contact_name.setText("Name: " + poster.name);

        contact_number = findViewById(R.id.judge_contact_number);
        contact_number.setText("Number: " + poster.number);

        contact_email = findViewById(R.id.judge_contact_email);
        contact_email.setText("Email: " + poster.email);


        locations = new CheckBox[8];
        for (int i = 0; i < locations.length; i++) {
            int resID = getResources().getIdentifier(String.format("judge_building_%s", i + 1), "id", getPackageName());
            locations[i] = findViewById(resID);
        }


    }
}
