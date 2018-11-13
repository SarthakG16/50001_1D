package com.example.sarth.smartmirrorapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadActivity extends AppCompatActivity{

    private ImageView poster_image;
    private ImageView enlarged_poster;
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

    private String category;
    private String locations_checked ="";
    private String title;

    private final static String TAG = "Logcat";
    private final static int PICK_FILE_REQUEST = 1;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        //Instantiating all the widgets

        poster_image = findViewById(R.id.poster_image);
        upload_poster = findViewById(R.id.upload_poster);
        enlarged_poster = findViewById(R.id.enlarged_poster);



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

        //Expanded poster image
        poster_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Entering enlarged poster");
                zoomImageForPoster();
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);



        //Spinner for categories begin
        String[] cat = {"Select your Category","Food","Announcement","Workshop","Welfare","Talks/Seminar","Others"};

        List<String> categories =  new ArrayList<>(Arrays.asList(cat));

        ArrayAdapter<String> adapter_categories = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item,
                categories);
        adapter_categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categories.setAdapter(adapter_categories);

        category = spinner_categories.getSelectedItem().toString();
        //Spinner for categories begin

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
                    Toast.makeText(UploadActivity.this,"Please enter your name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!number_correct){
                    Toast.makeText(UploadActivity.this,"Please enter your number",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!email_correct){
                    Toast.makeText(UploadActivity.this,"Please enter your email",Toast.LENGTH_LONG).show();
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
        if (requestCode == PICK_FILE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPoster();
        }
        else{
            Toast.makeText(UploadActivity.this, "Please provide permission", Toast.LENGTH_LONG).show();
        }
    }

    private void selectPoster(){
        Intent intent = new Intent();
        intent.setType("image/*");
        //TODO: more Types
        //String[] mimetypes = {"image/*", "application/pdf"};
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
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
            //Bitmap bitmap = ImageLoader.init().from(data.getPackage());
            poster_image.setImageURI(posterUri);

        }
        else{
            Toast.makeText(UploadActivity.this, "Please upload your poster.", Toast.LENGTH_LONG).show();
        }
    }

    //TODO: Generate ImageView for poster
    private void zoomImageForPoster() {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        if (posterUri != null){
            enlarged_poster.setImageURI(posterUri);
        }
        else{
            enlarged_poster.setImageResource(R.drawable.poster_holder);
        }

        enlarged_poster.setVisibility(View.VISIBLE);

        /*
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        poster_image.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        poster_image.setAlpha(0f);
        enlarged_poster.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        enlarged_poster.setPivotX(0f);
        enlarged_poster.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(enlarged_poster, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(enlarged_poster, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(enlarged_poster, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(enlarged_poster,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        */

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        //final float startScaleFinal = startScale;
        enlarged_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Load the high-resolution "zoomed-in" image.
                if (posterUri != null){
                    enlarged_poster.setImageURI(posterUri);
                }
                else{
                    enlarged_poster.setImageResource(R.drawable.poster_holder);
                }

                enlarged_poster.setVisibility(View.INVISIBLE);
                Log.i(TAG, "Exiting enlarged poster");

                /*
                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(enlarged_poster, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(enlarged_poster,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(enlarged_poster,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(enlarged_poster,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        poster_image.setAlpha(1f);
                        enlarged_poster.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        poster_image.setAlpha(1f);
                        enlarged_poster.setVisibility(View.GONE);
                        Log.i(TAG, "Exiting enlarged poster");
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
                */
            }
        });
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
