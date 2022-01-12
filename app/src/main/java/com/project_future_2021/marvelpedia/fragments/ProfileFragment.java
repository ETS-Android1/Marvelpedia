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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;

    private TextInputEditText profileUserName;
    private TextInputEditText profileFirstName;
    private TextInputEditText profilePassword;
    private TextInputEditText profileEmail;

    private Button btnSave;
    private Button btnShare;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {

        profileUserName = view.findViewById(R.id.profile_username_value);
        profileFirstName = view.findViewById(R.id.profile_first_name_value);
        profilePassword = view.findViewById(R.id.profile_password_value);
        profileEmail = view.findViewById(R.id.profile_email_value);

        btnSave = view.findViewById(R.id.profile_button_save);
        btnShare = view.findViewById(R.id.profile_button_share);

        SharedPreferences sp = requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE);

        String registerUsername = sp.getString("registerUsername", "");
        String registerName = sp.getString("registerName", "");
        String registerPassword = sp.getString("registerPassword", "");
        String registerEmail = sp.getString("registerEmail", "");

        profileUserName.setText(registerUsername);
        profileFirstName.setText(registerName);
        profilePassword.setText(registerPassword);
        profileEmail.setText(registerEmail);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    Toast.makeText(requireActivity(), "Save Success", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("registerUsername", profileUserName.getText().toString());
                    editor.putString("registerName", profileFirstName.getText().toString());
                    editor.putString("registerPassword", profilePassword.getText().toString());
                    editor.putString("registerEmail", profileEmail.getText().toString());

                    editor.apply();

                } else {
                    Toast.makeText(requireActivity(), "Save Failed", Toast.LENGTH_LONG).show();
                }
                closeKeyboard();
            }
        });

        // What happens when users click this button?
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                // The Title
                String body = "Share app Marvelpedia";
                // The description
                String sub = "I am Playing with Marvelpedia and i am very excited. You can check it out and download it at the link below. \uD83E\uDD29 ";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, body);
                myIntent.putExtra(Intent.EXTRA_TEXT, sub);
                // The Title of createChooser.
                startActivity(Intent.createChooser(myIntent, "Share Marvelpedia"));
            }
        });
    }


    private boolean validateInput() {
        if (profileUserName.getText().toString().equals("")) {
            profileUserName.setError("Please Enter Your Marvel Name");
            return false;
        }

        if (profileFirstName.getText().toString().equals("")) {
            profileFirstName.setError("Please Enter Your First Name");
            return false;
        }

        if (profilePassword.getText().toString().equals("")) {
            profilePassword.setError("Please Enter Your Password");
            return false;
        }

        if (profileEmail.getText().toString().equals("")) {
            profileEmail.setError("Please Enter Email");
            return false;
        }

        if (!(isMailValid(profileEmail.getText().toString()))) {
            profileEmail.setError("Please Enter Valid Email");
            return false;
        }
        return true;
    }

    private boolean isMailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method for closing the keyboard.
    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}




