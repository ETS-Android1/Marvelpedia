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
import com.google.android.material.transition.MaterialFadeThrough;
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

        viewInitializations();
    }

    void viewInitializations() {
        firstname = findViewById(R.id.linear_first_name);
        email = findViewById(R.id.linear_email);
        lastname = findViewById(R.id.linear_last_name);
        marvelname = findViewById(R.id.linear_marvel_name);
    }

    private TextInputEditText findViewById(int linear_first_name) {
        return null;
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
        if (lastname.getText().toString().equals("")) {
            lastname.setError("Please Enter Last Name");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button editbutton = (Button) findViewById(R.id.linear_edit);
        Button btnShare = (Button) findViewById(R.id.btnShare);

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show();
            }
        });
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




