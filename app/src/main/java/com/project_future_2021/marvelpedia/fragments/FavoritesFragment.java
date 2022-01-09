package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
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
    private MyListAdapter favoritesAdapter;

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

        // Part 1 of 2 of exactly what we did in our HeroesFragment too
        // in order to have nice transitions from Favorites to Details Fragment AND BACK.
        postponeEnterTransition();
        final ViewGroup parentView = (ViewGroup) view.getParent();

        TextView favorites_background_text = view.findViewById(R.id.favorites_txt);

        favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);
        favoritesAdapter = new MyListAdapter(new MyListAdapter.HeroDiff(), new ArrayList<>(), new MyListAdapter.myClickListener() {
            @Override
            public void onClick(View v, Hero data) {
                // What happens when users click on items-heroes.
                NavDirections action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(data);

                FragmentNavigator.Extras.Builder extrasBuilder = new FragmentNavigator.Extras.Builder();

                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroName), "nameTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroDescription), "descriptionTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedImageViewHeroThumbnail), "thumbnailTN");

                //Navigation.findNavController(view).navigate(action.getActionId(), action.getArguments(), null, extras);
                Navigation.findNavController(view).navigate(action, extrasBuilder.build());
            }

            @Override
            public void onFavoritePressed(View v, Hero heroSelected, int position) {
                // in order to be in this Fragment, the Hero WAS favorite, make him un-favorite.
                heroSelected.setFavorite(false);
                secondHeroesViewModel.updateHero(heroSelected);
                // TODO here or in the Adapter?
                favoritesAdapter.notifyItemChanged(position);
                //heroesAdapter.notifyDataSetChanged();
                Log.d(TAG, "onFavoritePressed: He was favorite, now he is not.");
            }
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        secondHeroesViewModel.getVmAllHeroesCombined().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> favoriteHeroes) {
//                if (favoriteHeroes.isEmpty()){
//                    favorites_background_text.setText(R.string.favorites_default_text);
//                }
//                else{
//                    favorites_background_text.setText("");
//                }

                ArrayList<Hero> favsList = new ArrayList<>();
                for (Hero hero : favoriteHeroes) {
                    if (hero.getFavorite()) {
                        favsList.add(hero);
                    }
                }
                favoritesAdapter.submitList(favsList);
                //favoritesAdapter.submitList(favoriteHeroes.stream().filter(Hero::getFavorite).collect(Collectors.toList()));

                // Part 2 of 2, see comment further up.
                parentView.getViewTreeObserver()
                        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                parentView.getViewTreeObserver()
                                        .removeOnPreDrawListener(this);
                                startPostponedEnterTransition();
                                return true;
                            }
                        });
            }
        });
    }
}