package com.project_future_2021.marvelpedia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.project_future_2021.marvelpedia.fragments.RegistrationBottomSheetFragment;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initViews();
    }


    private void initViews() {
        setupLoginButton();
        setupRegisterButton();
    }

    private void setupLoginButton() {
        TextInputEditText username = findViewById(R.id.login_username_value);
        TextInputEditText password = findViewById(R.id.login_password_value);

        // Actions that happen when the "Let's go" button is pressed.
        Button btnLogin = findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Credentials missing", Toast.LENGTH_SHORT).show();
                } else {
                    //Validation method is called
                    boolean isValid = validate(inputUsername, inputPassword);
                    // If validation process failed, just let the Users know.
                    if (!isValid) {
                        Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                    }
                    // If it was successful, navigate them to the MainActivity.
                    else {
                        Toast.makeText(LoginActivity.this, "Login is successful", Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    }
                }
            }
        });
    }

    private void setupRegisterButton() {
        // Bottom Sheet fragment of registration is called
        Button btnRegister = findViewById(R.id.register_account);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationBottomSheetFragment bottomSheetFragment = new RegistrationBottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), "RegisterBottomSheetFragment");
            }
        });
    }

    // Calls main activity and closes this one (LoginActivity).
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Validates the credentials the Users give.
    private boolean validate(String inputUsername, String inputPassword) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE);
        String registerUsername = sp.getString("registerUsername", "");
        String registerEmail = sp.getString("registerEmail", "");
        String registerPassword = sp.getString("registerPassword", "");

        return (inputUsername.equalsIgnoreCase(registerUsername) || inputUsername.equalsIgnoreCase(registerEmail)) && inputPassword.equals(registerPassword);
    }
}