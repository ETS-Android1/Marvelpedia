package com.project_future_2021.marvelpedia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.project_future_2021.marvelpedia.fragments.RegisterBottomSheetFragment;

public class LoginActivity extends AppCompatActivity {

    boolean isValid = false;
    private Button button;
    private Button regButton;
    private TextInputEditText username;
    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.login_username_value);
        password = findViewById(R.id.login_password_value);

        //Actions that happen when the "Let's go" button is pressed
        button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Info are missing", Toast.LENGTH_SHORT).show();
                } else {

                    //Validation method is called
                    isValid = validate(inputUsername, inputPassword);

                    if (!isValid) {

                        Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(LoginActivity.this, "Login is successful", Toast.LENGTH_SHORT).show();

                        openMainActivity();


                    }

                }


            }
        });

        //Bottom Sheet fragment of registration is called
        regButton = findViewById(R.id.register_account);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterBottomSheetFragment bottomSheet = new RegisterBottomSheetFragment();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

    }

    //Calls main activity
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();


    }

    //Validates data
    private boolean validate(String inputUsername, String inputPassword) {

        SharedPreferences sp = getApplicationContext().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE);
        String registerUsername = sp.getString("registerUsername","");
        String registerPassword = sp.getString("registerPassword","");


        if(inputUsername.equals(registerUsername) && inputPassword.equals(registerPassword)){
            return true;
        }
            return false;

    }
}