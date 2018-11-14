package com.example.sarth.smartmirrorapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadActivity extends AppCompatActivity{

    private ImageView poster_image;
    private Button upload_poster;
    private Uri posterUri;
    private Spinner spinner_categories;
    private CheckBox location_1;
    private CheckBox location_2;
    private CheckBox location_3;
    private CheckBox location_4;
    private CheckBox location_5;
    private CheckBox location_6;
    private CheckBox location_7;
    private CheckBox location_8;
    private TextInputLayout titleInput;
    private TextInputLayout contact_name;
    private TextInputLayout contact_number;
    private TextInputLayout contact_email;
    private Button upload_button;
    private Button poster_preview;

    private String category;
    private String locations_checked ="";
    private String title;
    private PDFView pdfView;
    private final static String TAG = "Logcat";
    private final static int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //Instantiating all the widgets


        upload_poster = findViewById(R.id.upload_poster);
        Log.i(TAG,"Reached");
        //PDF related widgets
        pdfView = findViewById(R.id.pdfView);
        poster_preview = findViewById(R.id.poster_preview);
        spinner_categories = findViewById(R.id.spinner_categories);

        titleInput =findViewById(R.id.upload_title);

        location_1 = findViewById(R.id.upload_building_1);
        location_2 = findViewById(R.id.upload_building_2);
        location_3 = findViewById(R.id.upload_building_3);
        location_4 = findViewById(R.id.upload_building_4);
        location_5 = findViewById(R.id.upload_building_5);
        location_6 = findViewById(R.id.upload_building_6);
        location_7 = findViewById(R.id.upload_building_7);
        location_8 = findViewById(R.id.upload_building_8);

        final CheckBox[] locations_arr = {location_1,location_2,location_3,location_4,location_5,location_6,location_7,location_8};
        final List<CheckBox> locatons_lst = new ArrayList<>(Arrays.asList(locations_arr));

        contact_name = findViewById(R.id.upload_contact_name);
        contact_number = findViewById(R.id.upload_contact_number);
        contact_email = findViewById(R.id.upload_contact_email);
        upload_button= findViewById(R.id.upload_confirm_button);

        //Spinner for categories begin
        String[] cat = {"Select your Category","Food","Announcement","Workshop","Welfare","Talks/Seminar","Others"};

        List<String> categories =  new ArrayList<>(Arrays.asList(cat));

        ArrayAdapter<String> adapter_categories = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item,
                categories);
        adapter_categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categories.setAdapter(adapter_categories);

        category = spinner_categories.getSelectedItem().toString();


        //Select poster
        upload_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPoster();
                }
                else{
                    String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(UploadActivity.this, permission , 9);
                }
            }
        });

        //Poster Preview
        poster_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Button Clicked");
                if (posterUri == null) {
                    return;
                }
                AlertDialog.Builder mBuilder =  new AlertDialog.Builder(UploadActivity.this);
                View poster_layout = getLayoutInflater().inflate(R.layout.dialog_poster,null);
                PDFView poster_expanded = poster_layout.findViewById(R.id.poster_expanded);
                poster_expanded.fromUri(posterUri).load();
                mBuilder.setView(poster_layout);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });



        //Upload Confirm Button
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean terminate = true;

                boolean name_correct = validate(contact_name);
                boolean number_correct =validate(contact_number);
                boolean email_correct = validate(contact_email);
                boolean title_correct = validate(titleInput);

                category = spinner_categories.getSelectedItem().toString();

                if(!title_correct) { //If no title is specified
                    Toast.makeText(UploadActivity.this, "Please Specify a Title.", Toast.LENGTH_LONG).show();
                    return;
                }

                //if not poster is selected
                if(posterUri == null){
                    Toast.makeText(UploadActivity.this, "Please upload your poster.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (category.equals("Select your Category")) {
                    Toast.makeText(UploadActivity.this, "Please Choose a Category.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!name_correct){
                    Toast.makeText(UploadActivity.this,"Please enter your name.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!number_correct){
                    Toast.makeText(UploadActivity.this,"Please enter your number.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!email_correct){
                    Toast.makeText(UploadActivity.this,"Please enter your email.",Toast.LENGTH_LONG).show();
                    return;
                }
                for (CheckBox c : locatons_lst) {
                    if (c.isChecked()) {
                        terminate = false;
                        break;
                    }
                }

                if(terminate) { // If no checkboxes are clicked
                    Toast.makeText(UploadActivity.this, "Please choose at least one location.", Toast.LENGTH_LONG).show();
                    return;
                }

                for (CheckBox c : locatons_lst) {
                    if (c.isChecked()) {
                        if(locations_checked.equals("")) {
                            locations_checked += c.getText();
                        } else {
                            if(!locations_checked.contains(c.getText())) {
                                locations_checked += ", " + c.getText();
                            }
                        }
                    }
                }

                Toast.makeText(UploadActivity.this, locations_checked, Toast.LENGTH_LONG).show();
            }
        });

    }

    //TODO: Upload data to database


    //make sure that permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPoster();
        }
        else{
            Toast.makeText(UploadActivity.this, "Please provide permission", Toast.LENGTH_LONG).show();
        }
    }

    private void selectPoster(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        //TODO: more Types
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Log.i(TAG,"Moving to upload poster");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    //check if user has selected a file or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null){
            posterUri = data.getData();
            if(posterUri == null) {
                Log.i(TAG,"Uri for poster returned null");
            }
            pdfView.fromUri(posterUri).load();

 /*           InputStream posterInputStream = null;
            try {
                posterInputStream = getContentResolver().openInputStream(posterUri);
                Log.i(TAG,posterInputStream.toString());
                Bitmap plantPoster = BitmapFactory.decodeStream(posterInputStream);
                poster_image.setImageBitmap(plantPoster);
            } catch (FileNotFoundException e) {
                Log.i(TAG,"OH NO");
                e.printStackTrace();
            }*/
        }
    }




    private boolean validate(TextInputLayout check) {
        String emailInput = check.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            check.setError("Field can't be empty");
            return false;
        } else {
            check.setError(null);
            return true;
        }
    }
}
