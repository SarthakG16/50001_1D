package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.CookieManager;
import java.net.CookieHandler;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "Logcat";

    private TextInputLayout login_username;
    private TextInputLayout login_password;
    private CheckBox login_checkbox;

    private String username;
    private String password;

    private Button login_enter_button;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.mainsharedprefs2";
    public static final String USER_KEY = "User_key";
    public static final String PWD_KEY = "Pwd_key";
    public static final String RMB_KEY = "Rmb_key";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE); // PREFS

        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_enter_button = findViewById(R.id.login_enter_button);
        login_checkbox = findViewById(R.id.login_checkbox);

        login_username.getEditText().setText(mPreferences.getString(USER_KEY,""));
        login_password.getEditText().setText(mPreferences.getString(PWD_KEY,""));
        login_checkbox.setChecked(mPreferences.getBoolean(RMB_KEY,false));

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        login_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean username_correct = validate(login_username);
                boolean password_correct = validate(login_password);
                if (!username_correct) {
                    Toast.makeText(MainActivity.this, "Please enter your username.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password_correct) {
                    Toast.makeText(MainActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
                    return;
                }

                username = login_username.getEditText().getText().toString().trim();
                password = login_password.getEditText().getText().toString().trim();

                //try as admin
                final HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("requested_privilege", "administrator");
                Request req_admin = new Request("POST", "auth/login", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Gson g1  = new Gson();
                        final Map<String, String> login_info1 = g1.fromJson(response, Map.class);
                        if (login_info1.get("status").equals("success")) {
                            Intent toAdmin = new Intent(MainActivity.this, AdminActivity.class);
                            toAdmin.putExtra(USER_KEY,username);
                            Toast.makeText(MainActivity.this,"Signed in as Admin",Toast.LENGTH_LONG).show();
                            startActivity(toAdmin);
                        } else {
                            params.put("requested_privilege", "user");
                            Request req_user = new Request("POST", "auth/login", params, new Request.Callback() {
                                @Override
                                public void onResponse(String response) {
                                    Gson g2 = new Gson();
                                    Map<String,String> login_info2 = g2.fromJson(response,Map.class);
                                    if (login_info2.get("status").equals("success")) {
                                        Intent toGuest = new Intent(MainActivity.this, GuestActivity.class);
                                        toGuest.putExtra(USER_KEY,username);
                                        Toast.makeText(MainActivity.this,"Signed in as Gust",Toast.LENGTH_LONG).show();
                                        startActivity(toGuest);
                                    } else {
                                        Toast.makeText(MainActivity.this,login_info2.get("error_message"), Toast.LENGTH_SHORT).show();
                                        login_username.setError("Incorrect Username or Password");
                                        login_password.setError("Incorrect Username or Password");
                                    }
                                }
                            });
                            req_user.execute();
                        }

                    }
                });
                req_admin.execute();
            }


        });

        //CHEAT BUTTONS TO BE REMOVED
        Button admin_cheat_button = (Button) findViewById(R.id.admin_cheat_button);
        Button guest_cheat_button = (Button) findViewById(R.id.guest_cheat_button);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        admin_cheat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cheatAdmin = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(cheatAdmin);
            }
        });
        guest_cheat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cheatGuest = new Intent(MainActivity.this, GuestActivity.class);
                startActivity(cheatGuest);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> params = new HashMap<>();
                params.put("username", "admin1");
                params.put("password", "hahaha");
                params.put("requested_privilege", "administrator");
                Request req = new Request("POST", "auth/login", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                    }
                });
                req.execute();
            }
        });
    }

    private boolean validate(TextInputLayout check) {
        String textInput = check.getEditText().getText().toString().trim();

        if (textInput.isEmpty()) {
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
        SharedPreferences.Editor editor = mPreferences.edit();
        if (!login_checkbox.isChecked()) {
            editor.clear();
            editor.apply();
            return;
        }
        editor.putString(USER_KEY,login_username.getEditText().getText().toString());
        editor.putString(PWD_KEY,login_password.getEditText().getText().toString());
        editor.putBoolean(RMB_KEY,login_checkbox.isChecked());
        editor.apply();
    }
}