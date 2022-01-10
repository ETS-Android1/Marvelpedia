package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private Button editbutton;
    private TextInputEditText firstname;
    private TextInputEditText lastname;
    private TextInputEditText email;
    private TextInputEditText marvelname;
    private Button btnShare;
    private Bundle savedInstanceState;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewInitializations(view);
    }

    void viewInitializations(View view) {
        firstname = view.findViewById(R.id.linear_first_name_value);
        email = view.findViewById(R.id.linear_email_value);
        marvelname = view.findViewById(R.id.linear_marvel_name_value);
        editbutton = view.findViewById(R.id.linear_edit);
        btnShare =  view.findViewById(R.id.btnShare);

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Edit", Toast.LENGTH_LONG).show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Share", Toast.LENGTH_LONG).show();
            }
        });
    }


    boolean validateInput() {

        if (firstname.getText().toString().equals("")) {
            firstname.setError("Please Enter First Name");
            return false;
        }
        if (email.getText().toString().equals("")) {
            email.setError("Please Enter Email");
            return false;
        }

        if (marvelname.getText().toString().equals("")) {
            marvelname.setError("Please Enter Marvel Name");
            return false;
        }

        if (!isemailValid(email.getText().toString())) {
            email.setError("Please Enter Valid Email");
            return false;
        }
        return true;

    }

    boolean isemailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }




}
// ignore these
        /*TextView profile_txt = view.findViewById(R.id.profile_txt);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                profile_txt.setText(s);
            }
        });

        ImageView image = view.findViewById(R.id.imageView2);

        Glide.with(this)
                .load(R.drawable.gif1)
                .into(image);*/




