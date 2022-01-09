package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.recycler_view.MyListAdapter;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;
import com.project_future_2021.marvelpedia.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private static final String REQUEST_TAG = "SearchFragmentRequest";
    private SearchViewModel searchViewModel;
    private HeroesViewModel thirdHeroesViewModel;
    private RecyclerView recyclerView;
    private MyListAdapter searchAdapter;
    private EditText userInputForHeroName;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        thirdHeroesViewModel = new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

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

        recyclerView = view.findViewById(R.id.search_recycler_view);
        searchAdapter = new MyListAdapter(new MyListAdapter.HeroDiff(), new ArrayList<>(), new MyListAdapter.myClickListener() {
            @Override
            public void onClick(View v, Hero data) {
                // What happens when people press on items(heroes) of the recycler view.
            }

            @Override
            public void onFavoritePressed(View v, Hero data, int position) {

            }
        });
        recyclerView.setAdapter(searchAdapter);

        // What happens when server returns list of heroes.
        thirdHeroesViewModel.getVmListOfHeroesTheUserSearchedFor().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> resultHeroes) {
                searchAdapter.submitList(resultHeroes);
            }
        });

        EditText userTyped = view.findViewById(R.id.editTextSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // What happens when user hits the Search button.
                String heroName = userTyped.getText().toString();
                thirdHeroesViewModel.searchForHeroesWithName(heroName, REQUEST_TAG);
            }
        });

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdHeroesViewModel.deleteAllHeroes();
            }
        });
        // ignore these...
        /*final String[] answer = {""};
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

            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thirdHeroesViewModel.insertManyHeroes(thirdHeroesViewModel.getVmAllHeroesCombined().getValue());
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdHeroesViewModel.deleteAllHeroes();
            }
        });*/

    }
}