package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.data.Image;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;
import com.project_future_2021.marvelpedia.viewmodels.SearchViewModel;

import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private SearchViewModel searchViewModel;
    private HeroesViewModel thirdHeroesViewModel;

    /*@Nullable
    private HeroRoomDatabase database;*/

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        thirdHeroesViewModel = new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        //database = Room.databaseBuilder(requireContext(), HeroRoomDatabase.class, "Marvelpedia").build();
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MaterialFadeThrough exitTransition = new MaterialFadeThrough();
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView search_txt = view.findViewById(R.id.search_txt);
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                search_txt.setText(s);
            }
        });


        final String[] answer = {""};
        TextView textFeedback = view.findViewById(R.id.textFeedback);
        Button btnInsert = view.findViewById(R.id.btnInsert);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnShow = view.findViewById(R.id.btnShow);

        Image temp_img = new Image("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784", "jpg");
        Hero temp_hero = new Hero(007, "onoma", "perigrafi", temp_img, true);
        //Hero temp_hero2 = thirdHeroesViewModel.getClAllHeroes();

        final String[] temp_text = {""};
        thirdHeroesViewModel.getVmAllHeroesCombined().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> heroes) {
                temp_text[0] = "";
                for (Hero hero : heroes) {
                    temp_text[0] = temp_text[0].concat(hero.getName() + " " + hero.getFavorite() + "\n");
                }
                textFeedback.setText(temp_text[0]);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new AsyncGetAllHeroesFromDb(database, new AsyncGetAllHeroesFromDb.Listener() {
                    @Override
                    public void onResult(List<Hero> result) {
                        textFeedback.setText(result.toString());
                    }
                }).execute();*/
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new AsyncInsertToDb(database, new AsyncInsertToDb.Listener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            Log.d(TAG, "onResult: Successful insert to DB");
                        } else {
                            Log.e(TAG, "onResult: Failed insert to DB");
                        }
                    }
                }).execute(thirdHeroesViewModel.getLiveDataHeroesList().getValue());*/
                //thirdHeroesViewModel.clInsert(temp_hero);
                thirdHeroesViewModel.insertManyHeroes(thirdHeroesViewModel.getVmAllHeroesCombined().getValue());
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new AsyncDeleteFromDb(database, new AsyncDeleteFromDb.Listener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            Log.d(TAG, "onResult: Successful delete from DB");
                        } else {
                            Log.e(TAG, "onResult: Failed delete from DB");
                        }
                    }
                }).execute();*/
                thirdHeroesViewModel.deleteAllHeroes();
            }
        });

    }
}