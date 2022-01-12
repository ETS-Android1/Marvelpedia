package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.recycler_view.MyListAdapter;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";
    private HeroesViewModel heroesViewModel;
    private MyListAdapter favoritesAdapter;
    private ViewGroup parentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        heroesViewModel = new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {
        parentView = (ViewGroup) view.getParent();
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);

        // Set a touch-helper for the Recycler View
        // Listen for swipe-left and swipe-right gestures of the users.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // We don't care about this method.
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // We care about this one.
            // Get the Hero the user swiped at, make him un-favorite, and update the Database.
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Hero heroTobeUnFavorite = favoritesAdapter.getHeroAt(viewHolder.getAbsoluteAdapterPosition());
                heroTobeUnFavorite.setFavorite(false);
                heroesViewModel.updateHero(heroTobeUnFavorite);
            }
        }).attachToRecyclerView(favoritesRecyclerView);


        favoritesAdapter = new MyListAdapter(new MyListAdapter.HeroDiff(), new ArrayList<>(), new MyListAdapter.myClickListener() {
            // Handle clicks anywhere on the Hero BUT his favorites icon.
            @Override
            public void onClick(View v, Hero data) {
                // Goto DetailsFragment (following instructions specified in nav_graph.xml)
                NavDirections actionFavoritesToDetails = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(data);

                FragmentNavigator.Extras.Builder extrasBuilder = new FragmentNavigator.Extras.Builder();

                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroName), "nameTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroDescription), "descriptionTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedImageViewHeroThumbnail), "thumbnailTN");

                Navigation.findNavController(view).navigate(actionFavoritesToDetails, extrasBuilder.build());
            }

            // Handle clicks on his favorites icon.
            @Override
            public void onFavoritePressed(View v, Hero heroSelected, int position) {
                // in order to be in this Fragment, the Hero WAS favorite, make him un-favorite, and update the Database afterwards.
                heroSelected.setFavorite(false);
                heroesViewModel.updateHero(heroSelected);
                // TODO here or in the Adapter?
                //favoritesAdapter.notifyItemChanged(position);
            }
        });
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Observe our all-Heroes list in the heroesViewModel.
        heroesViewModel.getVmAllHeroesCombined().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            // Whenever a change in that list happens (ie a Hero was added/updated), filter that list, and show the Heroes with isFavorite() == true
            @Override
            public void onChanged(List<Hero> allHeroesList) {
                // Part 1 of 2 of exactly what we did in our HeroesFragment too
                // in order to have nice transitions from Favorites to Details Fragment AND BACK.
                postponeEnterTransition();

                ArrayList<Hero> filteredForFavoriteList = new ArrayList<>();
                for (Hero hero : allHeroesList) {
                    if (hero.getFavorite()) {
                        filteredForFavoriteList.add(hero);
                    }
                }
                favoritesAdapter.submitList(filteredForFavoriteList);
                //favoritesAdapter.submitList(allHeroesList.stream().filter(Hero::getFavorite).collect(Collectors.toList()));

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