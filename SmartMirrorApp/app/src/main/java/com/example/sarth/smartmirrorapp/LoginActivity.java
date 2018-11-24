package com.example.sarth.smartmirrorapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity{
    private TextInputLayout login_username;
    private TextInputLayout login_password;

    private String username;
    private String password;

    private Button login_enter_button;

    protected void onCreate(){
        setContentView(R.layout.activity_login);
        login_username = (TextInputLayout) findViewById(R.id.login_username);
        login_password = (TextInputLayout) findViewById(R.id.login_password);
        login_enter_button = (Button) findViewById(R.id.login_enter_button);

        login_enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean username_correct = validate(login_username);
                boolean password_correct = validate(login_password);
                if (!username_correct){
                    Toast.makeText(LoginActivity.this,"Please enter your username.",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password_correct){
                    Toast.makeText(LoginActivity.this,"Please enter your password.",Toast.LENGTH_LONG).show();
                    return;
                }

                username = login_username.getEditText().getText().toString().trim();
                password = login_password.getEditText().getText().toString().trim();

                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                try{
                    params.put("requested_privilege", "administrator");
                    Request req = new Request("POST","auth/login", params, new Request.Callback () {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("success")) {
                                Intent toAdmin = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(toAdmin);
                            }
                        }
                    });
                } catch(Exception ex){
                    params.put("requested_privilege","user");
                    Request req = new Request("POST","auth/login", params, new Request.Callback () {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("success")) {
                                Intent toGuest = new Intent(LoginActivity.this, GuestActivity.class);
                                startActivity(toGuest);
                            }
                        }
                    });
                }
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
}