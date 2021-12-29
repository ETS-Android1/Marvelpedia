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
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.viewmodels.FavoritesViewModel;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";
    private FavoritesViewModel favoritesViewModel;
    private HeroesViewModel secondHeroesViewModel;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        secondHeroesViewModel = new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView favorites_txt = view.findViewById(R.id.favorites_txt);
        /*favoritesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                favorites_txt.setText(s);
            }
        });*/
        secondHeroesViewModel.getLiveDataHeroesList().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> heroes) {
                String s = "";
                for (Hero hero : heroes
                ) {
                    s += "Onoma: " + hero.getName() + "favorite?: " + hero.getFavorite() + "\n";
                }
                favorites_txt.setText(s);
            }
        });
    }

}