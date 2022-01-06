package com.project_future_2021.marvelpedia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.project_future_2021.marvelpedia.MainActivity;
import com.project_future_2021.marvelpedia.R;

public class RegisterBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "RegisterBottomSheetFragment";
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerUsername = view.findViewById(R.id.register_username_value);
        registerPassword = view.findViewById(R.id.register_password_value);
        confirmPassword = view.findViewById(R.id.confirm_password_value);
        button = view.findViewById(R.id.register_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkUsername = registerUsername.getText().toString();
                String checkFirstPassword = registerPassword.getText().toString();
                String checkSecondPassword = confirmPassword.getText().toString();

                if (checkUsername.isEmpty() || checkFirstPassword.isEmpty() || checkSecondPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Info are missing", Toast.LENGTH_SHORT).show();
                } else {

                    if (checkFirstPassword.equals(checkSecondPassword)) {

                        Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();

                        openMainActivity();

                    } else {

                        Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();


                    }
                }


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_bottom_sheet,
                container, false);

    }

    private void openMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


}