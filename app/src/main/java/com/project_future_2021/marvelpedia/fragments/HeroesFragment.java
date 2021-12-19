package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

public class HeroesFragment extends Fragment {

    private static final String TAG = "HeroesFragment";
    private HeroesViewModel heroesViewModel;

    public static HeroesFragment newInstance() {
        return new HeroesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        heroesViewModel = new ViewModelProvider(this).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.heroes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // when the user taps on the button, go to the DetailsFragment
        view.findViewById(R.id.heroes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsFragment detailsFragment = new DetailsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, detailsFragment).addToBackStack("details_fragment").commit();
            }
        });

        TextView heroes_txt = view.findViewById(R.id.heroes_txt);
        heroesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                heroes_txt.setText(s);
            }
        });
    }
}