package com.project_future_2021.marvelpedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.project_future_2021.marvelpedia.fragments.RegisterBottomSheetFragment;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private Button regButton;
    private TextInputEditText username;
    private TextInputEditText password;

    private String UserData = "test";
    private String PassData = "test";

    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.login_username_value);
        password = findViewById(R.id.login_password_value);

        button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();

                if(inputUsername.isEmpty() || inputPassword.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Info are missing", Toast.LENGTH_SHORT).show();
                }else{

                    isValid = validate(inputUsername, inputPassword);

                    if(!isValid){

                        Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(LoginActivity.this, "Login is successful", Toast.LENGTH_SHORT).show();

                        openMainActivity();

                        finish();

                    }

                }



            }
        });

        regButton = findViewById(R.id.register_account);
        regButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        RegisterBottomSheetFragment bottomSheet = new RegisterBottomSheetFragment();
                        bottomSheet.show(getSupportFragmentManager(),
                                "ModalBottomSheet");
                    }
                });

    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private boolean validate(String inputUsername, String inputPassword){

        if(inputUsername.equals(UserData) && inputPassword.equals(PassData)){
            return true;
        }
            return false;

    }
}