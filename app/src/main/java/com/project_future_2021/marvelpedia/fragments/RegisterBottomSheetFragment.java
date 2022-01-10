package com.project_future_2021.marvelpedia.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
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
    boolean isValid = false;
    private TextInputEditText registerEmail;
    private TextInputEditText registerName;
    private TextInputEditText registerUsername;
    private TextInputEditText registerPassword;
    private TextInputEditText confirmPassword;
    private Button button;
    SharedPreferences sp;

    public RegisterBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerEmail = view.findViewById(R.id.register_email_value);
        registerName = view.findViewById(R.id.register_name_value);
        registerUsername = view.findViewById(R.id.register_username_value);
        registerPassword = view.findViewById(R.id.register_password_value);
        confirmPassword = view.findViewById(R.id.confirm_password_value);
        button = view.findViewById(R.id.register_button);

        sp = getActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE);

        //What happens when the "Register" button is pressed
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkEmail = registerEmail.getText().toString();
                String checkName = registerName.getText().toString();
                String checkUsername = registerUsername.getText().toString();
                String checkFirstPassword = registerPassword.getText().toString();
                String checkSecondPassword = confirmPassword.getText().toString();
                isValid = validation();

                //Saving data from registration
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("registerUsername", checkUsername);
                editor.putString("registerEmail", checkEmail);
                editor.putString("registerPassword", checkFirstPassword);
                editor.putString("registerName", checkName);
                editor.commit();



                    if (!isValid) {

                        Toast.makeText(getActivity(), "Info are missing or are incorrect", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();

                        openMainActivity();

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
        requireActivity().onBackPressed();
    }

    private boolean validation(){
        String checkEmail = registerEmail.getText().toString();
        String checkName = registerName.getText().toString();
        String checkUsername = registerUsername.getText().toString();
        String checkFirstPassword = registerPassword.getText().toString();
        String checkSecondPassword = confirmPassword.getText().toString();
        if(checkEmail.isEmpty() || checkName.isEmpty() || checkUsername.isEmpty()){

            return false;

        }else{
            if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()) {
                registerEmail.setError(getResources().getString(R.string.invalid_email));
                return false;
            }else if (registerPassword.length()<6){
                registerPassword.setError(getResources().getString(R.string.invalid_password));
                return false;
            }else if(checkFirstPassword.equals(checkSecondPassword)){
                return true;
            }else{
                registerPassword.setError(getResources().getString(R.string.password_match));
                confirmPassword.setError(getResources().getString(R.string.password_match));
                return false;
            }

        }
    };


}