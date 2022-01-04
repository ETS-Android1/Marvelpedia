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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.MaterialFadeThrough;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.recycler_view.MyListAdapter;
import com.project_future_2021.marvelpedia.viewmodels.FavoritesViewModel;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";
    private FavoritesViewModel favoritesViewModel;
    private HeroesViewModel secondHeroesViewModel;
    private RecyclerView favoritesRecyclerView;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MaterialFadeThrough enterTransition = new MaterialFadeThrough();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView favorites_txt = view.findViewById(R.id.favorites_txt);

        favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);
        MyListAdapter favoritesAdapter = new MyListAdapter(new MyListAdapter.HeroDiff(), new ArrayList<>(), new MyListAdapter.myClickListener() {
            @Override
            public void onClick(View v, Hero data) {
                // What happens when users click on items-heroes.
            }
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        /*favoritesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                favorites_txt.setText(s);
            }
        });*/
        secondHeroesViewModel.getLiveDataHeroesList().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> heroes) {
                // What happens when changes in the list occur.
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