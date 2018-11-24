package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity{
    private TextInputLayout login_username;
    private TextInputLayout login_password;

    private Button login_enter_button;

    protected void onCreate(){
        setContentView(R.layout.activity_login);
        login_username = (TextInputLayout) findViewById(R.id.login_username);
        login_password = (TextInputLayout) findViewById(R.id.login_password);
        login_enter_button = (Button) findViewById(R.id.login_enter_button);

        login_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                Request req = new Request("POST","auth/login", params, new Request.Callback () {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")){
                            //Intent toHome = new Intent(LoginActivity.this,);
                        }

                    }
                });
            }
        });
    }
}