package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.viewmodels.LoginViewModel;


public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavDirections actionGoToHeroesFragment = LoginFragmentDirections.actionLoginFragmentToHeroesFragment();
//                Navigation.findNavController(view).navigate(actionGoToHeroesFragment);
            }
        });


    }
}