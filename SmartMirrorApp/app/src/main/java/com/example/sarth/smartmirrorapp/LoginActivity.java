package com.example.sarth.smartmirrorapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "Logcat";

    private TextInputLayout login_username;
    private TextInputLayout login_password;

    private String username;
    private String password;

    private Button login_enter_button;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_username = (TextInputLayout) findViewById(R.id.login_username);
        login_password = (TextInputLayout) findViewById(R.id.login_password);
        login_enter_button = (Button) findViewById(R.id.login_enter_button);

        login_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean username_correct = validate(login_username);
                boolean password_correct = validate(login_password);
                if (!username_correct) {
                    Toast.makeText(LoginActivity.this, "Please enter your username.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password_correct) {
                    Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
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
                            //Toast.makeText(LoginActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                            Intent toAdmin = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(toAdmin);
                        } else {
                            params.put("requested_privilege", "user");
                            Request req_user = new Request("POST", "auth/login", params, new Request.Callback() {
                                @Override
                                public void onResponse(String response) {
                                    Gson g2 = new Gson();
                                    Map<String,String> login_info2 = g2.fromJson(response,Map.class);
                                    if (login_info2.get("status").equals("success")) {
                                        //Toast.makeText(LoginActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                                        Intent toGuest = new Intent(LoginActivity.this, GuestActivity.class);
                                        startActivity(toGuest);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            req_user.execute();
                        }

                    }
                });
                req_admin.execute();
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
        });

        //CHEAT BUTTONS TO BE REMOVED
        Button admin_cheat_button = (Button) findViewById(R.id.admin_cheat_button);
        Button guest_cheat_button = (Button) findViewById(R.id.guest_cheat_button);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        admin_cheat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cheatAdmin = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(cheatAdmin);
            }
        });
        guest_cheat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cheatGuest = new Intent(LoginActivity.this, GuestActivity.class);
                startActivity(cheatGuest);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();

                HashMap<String, String> params = new HashMap<>();
                params.put("username", "admin1");
                params.put("password", "hahaha");
                params.put("requested_privilege", "administrator");
                Request req = new Request("POST", "auth/login", params, new Request.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(LoginActivity.this, "Received: " + response, Toast.LENGTH_SHORT).show();
                    }
                });
                        /*Request req = new Request("GET","posters", params, new Request.PostersCallback() {
                            @Override
                            public void onResponse(List<Poster> posters) {
                                Toast.makeText(MainActivity.this, "Received posters: " + posters.toString(), Toast.LENGTH_LONG).show();
                            }
                        });*/
                req.execute();
            }
        });
    }
}