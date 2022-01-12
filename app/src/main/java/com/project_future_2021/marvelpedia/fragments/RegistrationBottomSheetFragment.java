package com.project_future_2021.marvelpedia.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project_future_2021.marvelpedia.MainActivity;
import com.project_future_2021.marvelpedia.R;

public class RegistrationBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "RegistrationBottomSheetFragment";

    private TextInputEditText registerEmail;
    private TextInputEditText registerName;
    private TextInputEditText registerUsername;
    private TextInputEditText registerPassword;
    private TextInputEditText confirmPassword;
    private TextInputLayout emailValue;
    private TextInputLayout passwordValue;
    private TextInputLayout confirmValue;

    private String checkEmail;
    private String checkName;
    private String checkUsername;
    private String checkFirstPassword;
    private String checkSecondPassword;

    private Button btnRegister;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    // Initialize our views.
    private void initViews(View view) {
        registerEmail = view.findViewById(R.id.register_email_value);
        registerName = view.findViewById(R.id.register_name_value);
        registerUsername = view.findViewById(R.id.register_username_value);
        registerPassword = view.findViewById(R.id.register_password_value);
        confirmPassword = view.findViewById(R.id.confirm_password_value);
        emailValue = view.findViewById(R.id.register_email);
        passwordValue = view.findViewById(R.id.register_password);
        confirmValue = view.findViewById(R.id.confirm_password);

        btnRegister = view.findViewById(R.id.register_button);
        setupButtonRegister();
    }

    private void setupButtonRegister() {
        // What happens when the "Register" button is pressed.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUserInputValid = validateUserInput();

                if (!isUserInputValid) {
                    Toast.makeText(getActivity(), "Info is missing or incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();
                    saveUserInput();
                    openMainActivity();
                }
            }
        });
    }

    private boolean validateUserInput() {
        checkEmail = registerEmail.getText().toString().trim();
        checkName = registerName.getText().toString().trim();
        checkUsername = registerUsername.getText().toString().trim();
        checkFirstPassword = registerPassword.getText().toString();
        checkSecondPassword = confirmPassword.getText().toString();
        if (checkEmail.isEmpty() || checkName.isEmpty() || checkUsername.isEmpty()) {
            return false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches()) {
                emailValue.setError(getResources().getString(R.string.invalid_email));
                registerEmail.requestFocus();
                return false;
            } else if (checkFirstPassword.length() < 6) {
                passwordValue.setError(getResources().getString(R.string.invalid_password));
                registerPassword.requestFocus();
                return false;
            } else if (checkFirstPassword.equals(checkSecondPassword)) {
                return true;
            } else {
                passwordValue.setError(getResources().getString(R.string.password_match));
                confirmValue.setError(getResources().getString(R.string.password_match));
                registerPassword.requestFocus();
                return false;
            }
        }
    }

    // Saving data from registration.
    private void saveUserInput() {
        SharedPreferences sp = requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("registerUsername", checkUsername);
        editor.putString("registerEmail", checkEmail);
        editor.putString("registerPassword", checkFirstPassword);
        editor.putString("registerName", checkName);
        editor.apply();
    }

    // Navigate to the MainActivity,
    // close Registration Fragment and Login Activity,
    // so if the Users press the 'Back' button our App exits.
    private void openMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().onBackPressed();
    }
}