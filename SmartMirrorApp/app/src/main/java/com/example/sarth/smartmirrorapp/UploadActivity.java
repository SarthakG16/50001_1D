package com.example.sarth.smartmirrorapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UploadActivity extends AppCompatActivity{

    private boolean clear = false;
    private Uri posterUri;
    private Spinner spinner_categories;

    private CheckBox[] locations;

    private TextInputLayout titleInput;
    private TextInputLayout contact_name;
    private TextInputLayout contact_number;
    private TextInputLayout contact_email;

    private TextView date_start;
    private TextView date_stop;


    //BUTTONS
    private Button upload_poster;
    private Button upload_button;
    private Button poster_preview_button;
    private ImageButton start_date_button;
    private ImageButton stop_date_button;

    private String category;
    private String locations_checked ="";
    private String title;
    private String server_start_date;
    private String shared_start_date;
    private String server_stop_date;
    private String shared_stop_date;
    private String name;
    private String number;
    private String email;
    private String serialized_data;

    private Date start = Calendar.getInstance().getTime();
    private Date stop = null;

    private PDFView pdfView;
    private final static String TAG = "Logcat";
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String TITLE_KEY = "Title_Key";
    public static final String CAT_KEY = "Cat_Key";
    public static final String NAME_KEY = "Name_Key";
    public static final String NUMBER_KEY = "Number_Key";
    public static final String EMAIL_KEY = "Email_Key";
    public static final String START_DATE_KEY = "Date0_Key";
    public static final String STOP_DATE_KEY = "Date1_Key";

    private final static int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Preferences.

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Initialize title textView.

        titleInput = findViewById(R.id.upload_title);
        titleInput.getEditText().setText(mPreferences.getString(TITLE_KEY,""));

        // PDF related widgets.

        pdfView = findViewById(R.id.pdfView);
        poster_preview_button = findViewById(R.id.poster_preview_button);
        spinner_categories = findViewById(R.id.spinner_categories);

        // Initialize the select poster button.

        upload_poster = findViewById(R.id.upload_poster);

        // Initialize the category spibner.

        final String[] cat = { "Select your Category", "Food", "Announcement", "Workshop", "Welfare", "Talks/Seminar", "Others" };

        List<String> categories =  new ArrayList<>(Arrays.asList(cat));
        ArrayAdapter<String> adapter_categories = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item, categories);
        adapter_categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categories.setAdapter(adapter_categories);

        String temp_spinner_category = mPreferences.getString(CAT_KEY,"Select your Category");
        spinner_categories.setSelection(categories.indexOf(temp_spinner_category));

        // Initialize calendar buttons and the textView.

        start_date_button = findViewById(R.id.start_date_button);
        stop_date_button = findViewById(R.id.stop_date_button);

        date_start = findViewById(R.id.date_start);
        date_stop = findViewById(R.id.date_stop);

        date_start.setText(mPreferences.getString(START_DATE_KEY,"Start Date of screening Poster:" ));
        date_stop.setText(mPreferences.getString(STOP_DATE_KEY, "Stop Date of screening Poster:"));

        // Initialize contact details.

        contact_name = findViewById(R.id.upload_contact_name);
        contact_number = findViewById(R.id.upload_contact_number);
        contact_email = findViewById(R.id.upload_contact_email);

        contact_name.getEditText().setText(mPreferences.getString(NAME_KEY,""));
        contact_number.getEditText().setText(mPreferences.getString(NUMBER_KEY,""));
        contact_email.getEditText().setText(mPreferences.getString(EMAIL_KEY,""));

        // Initialize location checkboxes.

        locations = new CheckBox[8];
        for (int i = 0; i < locations.length; i++) {
            int resID = getResources().getIdentifier(String.format("upload_building_%s", i + 1), "id", getPackageName());
            locations[i] = findViewById(resID);
            locations[i].setChecked(mPreferences.getBoolean(String.format("BOX%s_KEY", i), false));
        }

        // Initialize the upload button.
        upload_button = findViewById(R.id.upload_confirm_button);


        //TODO: BUTTONS
        //Select poster
        upload_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPoster();
                }
                else{
                    String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(UploadActivity.this, permission , PICK_FILE_REQUEST);
                }
            }
        });

        //Poster Preview
        poster_preview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Button Clicked");
                if (posterUri == null) {
                    return;
                }
                AlertDialog.Builder mBuilder =  new AlertDialog.Builder(UploadActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                mBuilder.setTitle("Your Poster");
                View poster_layout = getLayoutInflater().inflate(R.layout.dialog_poster,null);
                PDFView poster_expanded = poster_layout.findViewById(R.id.poster_expanded);
                Log.i(TAG,String.valueOf(poster_expanded == null));
                poster_expanded.fromUri(posterUri).load();;
                mBuilder.setView(poster_layout);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //Calendars
        start_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                final int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog dpd_start = new DatePickerDialog(UploadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DATE, dayOfMonth);

                        if (stop != null) {
                            if (c.getTime().after(stop)) {
                                Toast.makeText(UploadActivity.this, "Please choose a Start Date before the Stop Date",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        start = c.getTime();
                        server_start_date = c.get(Calendar.YEAR) +"-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DATE) + " ";
                        server_start_date += "00:00:00";
                        shared_start_date = DateFormat.getDateInstance().format(c.getTime());
                        date_start.setText("Start date: " + shared_start_date);
                    }
                },year,month,day);
                dpd_start.show();
            }
        });

        stop_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog dpd_stop = new DatePickerDialog(UploadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DATE, dayOfMonth);
                        if(c.getTime().before(start)) {
                            Toast.makeText(UploadActivity.this,"Please choose a Stop date after the Start Date"
                            ,Toast.LENGTH_LONG).show();
                            return;
                        }
                        stop = c.getTime();
                        server_stop_date = c.get(Calendar.YEAR) +"-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DATE) + " ";
                        server_stop_date += "23:59:59";
                        shared_stop_date = DateFormat.getDateInstance().format(c.getTime());
                        date_stop.setText("Stop date: " + shared_stop_date);
                    }
                },year,month,day);
                dpd_stop.show();
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

                if (date_start.getText().toString().equals("Start Date of screening Poster:")) {
                    Toast.makeText(UploadActivity.this,"Please choose a start date.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(date_stop.getText().toString().equals("Stop Date of screening Poster:")) {
                    Toast.makeText(UploadActivity.this,"Please choose a stop date.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!name_correct){
                    Toast.makeText(UploadActivity.this,"Please enter your name",Toast.LENGTH_LONG).show();
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
                //If no date is entered.


                for (CheckBox c : locations) {
                    if (c.isChecked()) {
                        terminate = false;
                        break;
                    }
                }

                if(terminate) { // If no checkboxes are clicked
                    Toast.makeText(UploadActivity.this, "Please choose at least one location.", Toast.LENGTH_LONG).show();
                    return;
                }

                for (CheckBox c : locations) {
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

                title = titleInput.getEditText().getText().toString().trim();
                name = contact_name.getEditText().getText().toString().trim();
                number = contact_number.getEditText().getText().toString().trim();
                email = contact_email.getEditText().getText().toString().trim();
                serialized_data = "";

                try {
                    ContentResolver cr = getContentResolver();
                    InputStream is = cr.openInputStream(posterUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(is, baos);
                    byte[] result = baos.toByteArray();
                    is.close();
                    baos.close();
                    serialized_data = Base64.encodeToString(result);

                } catch (FileNotFoundException e) {
                    Log.i(TAG,"FileNotFound");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i(TAG,"IOexception");
                    e.printStackTrace();
                }

                Poster poster = new Poster(title,category,name,number,email,server_start_date,server_stop_date,locations_checked,serialized_data);
                Poster.requests.add(poster);
                HashMap<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("category", category);
                params.put("contact_name", name);
                params.put("contact_number", number);
                params.put("contact_email", email);
                params.put("date_posted", server_start_date);
                params.put("date_expiry", server_stop_date);
                params.put("locations", locations_checked);
                params.put("serialized_image_data", serialized_data);
                //TODO put the parameters in

                Request req = new Request("POST","posters/", params, new Request.Callback() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(UploadActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                        if (response.contains("Poster already exists with given title")) {
                            titleInput.setError("Title Already Exists");
                            Toast.makeText(UploadActivity.this,"Title already exists", Toast.LENGTH_LONG).show();
                        }
                        Log.i(TAG,response);
                    }
                });
                req.execute();
                //clear = true;

            }
        });

    }

    //TODO: Upload data to databas


    //make sure that permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == PICK_FILE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPoster();
        }
        else{
            Toast.makeText(UploadActivity.this, "Please provide permission", Toast.LENGTH_LONG).show();
        }
    }

    private void selectPoster(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
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

    @Override
    protected void onPause() {
            super.onPause();
            SharedPreferences.Editor editor =mPreferences.edit();
            if(clear) {
                editor.clear();
                editor.apply();
                return;
            }
            editor.putString(TITLE_KEY,titleInput.getEditText().getText().toString());
            editor.putString(CAT_KEY,spinner_categories.getSelectedItem().toString());
            editor.putString(NAME_KEY,contact_name.getEditText().getText().toString());
            editor.putString(NUMBER_KEY,contact_number.getEditText().getText().toString());
            editor.putString(EMAIL_KEY,contact_email.getEditText().getText().toString());
            editor.putString(START_DATE_KEY, date_start.getText().toString());
            editor.putString(STOP_DATE_KEY, date_stop.getText().toString());
            for (int i = 0; i < locations.length; i++) {
                editor.putBoolean(String.format("BOX%s_KEY", i), locations[i].isChecked());
            }
            editor.apply();
    }
}
