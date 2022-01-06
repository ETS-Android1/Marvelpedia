package com.project_future_2021.marvelpedia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.project_future_2021.marvelpedia.MainActivity;
import com.project_future_2021.marvelpedia.R;

public class RegisterBottomSheetFragment extends BottomSheetDialogFragment {

    private TextInputEditText registerUsername;
    private TextInputEditText registerPassword;
    private TextInputEditText confirmPassword;
    private Button button;

    public RegisterBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_bottom_sheet,
                container, false);

        registerUsername = v.findViewById(R.id.register_username);
        registerPassword = v.findViewById(R.id.register_password_value);
        confirmPassword = v.findViewById(R.id.confirm_password_value);
        button = v.findViewById(R.id.register_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkUsername = registerUsername.getText().toString();
                String checkFirstPassword = registerPassword.getText().toString();
                String checkSecondPassword = confirmPassword.getText().toString();

                if (checkUsername.isEmpty() || checkFirstPassword.isEmpty() || checkSecondPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Info are missing", Toast.LENGTH_SHORT).show();
                } else {

                    if (checkFirstPassword == checkSecondPassword) {

                        Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();

                        openMainActivity();

                    } else {

                        Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();


                    }
                }


            }
        });

        return v;

    }

    private void openMainActivity () {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


}