        package com.project_future_2021.marvelpedia.fragments;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.util.Patterns;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.ViewModelProvider;

        import com.google.android.material.textfield.TextInputEditText;
        import com.project_future_2021.marvelpedia.LoginActivity;
        import com.project_future_2021.marvelpedia.R;
        import com.project_future_2021.marvelpedia.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;
    private TextView name;
    private TextView inputName;
    private TextView email;
    private TextView inputEmail;
    private TextView username;
    private TextView inputUsername;
    private Button btnSave;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUsername = view.findViewById(R.id.input_username);
        inputEmail = view.findViewById(R.id.input_email);
        inputName = view.findViewById(R.id.input_name);
        btnSave = view.findViewById(R.id.btn_save);

        SharedPreferences sp = requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE);
        String registerUsername = sp.getString("registerUsername","");
        String registerEmail = sp.getString("registerEmail","");
        String registerName = sp.getString("registerName","");

        inputUsername.setText(registerUsername);
        inputEmail.setText(registerEmail);
        inputName.setText(registerName);

        btnSave = view.findViewById(R.id.btn_save);

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
            inputEmail.setError(getResources().getString(R.string.invalid_email));
            btnSave.setEnabled(false);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String checkEmail = inputEmail.getText().toString();
                    String checkName = inputName.getText().toString();
                    String checkUsername = inputUsername.getText().toString();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("registerUsername", checkUsername);
                    editor.putString("registerEmail", checkEmail);
                    editor.putString("registerName", checkName);
                    editor.commit();

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

