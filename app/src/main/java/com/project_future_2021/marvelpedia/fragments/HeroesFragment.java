package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.recycler_view.MyListAdapter;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.ArrayList;
import java.util.List;

public class HeroesFragment extends Fragment {

    private static final String TAG = "HeroesFragment";
    private static final String REQUEST_TAG = "HeroesFragmentRequest";
    private HeroesViewModel heroesViewModel;
    private MyListAdapter heroesAdapter;
    private ViewGroup parentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //"heroesViewModel" will attach(-live) to the getActivity()(i.e. MainActivity)'s lifecycle.
        // So it will survive HeroesFragment destruction when navigating to other fragments.
        // so, instead of   new ViewModelProvider(this).get(HeroesViewModel.class);
        // we use           new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        heroesViewModel = new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.heroes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {
        parentView = (ViewGroup) view.getParent();
        setupRecyclerView(view);
        setupProgressBar(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView heroesRecyclerView = view.findViewById(R.id.heroes_recycler_view);

        // Set a scroll listener to our RecyclerView
        // so we know when we have scrolled to the end of our Heroes list,
        // thus we need to load more Heroes.
        heroesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // direction 1 is downwards. Upwards would be -1.
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    heroesViewModel.loadMore();
                    Log.d(TAG, "onScrollStateChanged: We scrolled to the end of our Heroes list");
                }
            }
        });

        heroesAdapter = new MyListAdapter(new MyListAdapter.HeroDiff(), new ArrayList<>(), new MyListAdapter.myClickListener() {
            // Handle clicks anywhere on the Hero BUT his favorites icon.
            @Override
            public void onClick(View v, Hero data) {
                // Goto DetailsFragment (following instructions specified in nav_graph.xml)
                NavDirections actionHeroesToDetails = HeroesFragmentDirections.actionHeroesFragmentToDetailsFragment(data);

                FragmentNavigator.Extras.Builder extrasBuilder = new FragmentNavigator.Extras.Builder();

                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroName), "nameTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroDescription), "descriptionTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedImageViewHeroThumbnail), "thumbnailTN");

                Navigation.findNavController(view).navigate(actionHeroesToDetails, extrasBuilder.build());
            }

            // Handle clicks on his favorites icon.
            @Override
            public void onFavoritePressed(View v, Hero heroSelected, int position) {
                // If the Hero was favorite, make him un-favorite.
                // else, he wasn't favorite, make him favorite now.
                heroSelected.setFavorite(!heroSelected.getFavorite());
                // Don't forget to update the Database too.
                heroesViewModel.updateHero(heroSelected);
                // TODO here or in the Adapter?
                //heroesAdapter.notifyItemChanged(position);
            }
        });
        // That should -THEORETICALLY- scroll to the last position of the RecyclerView, but...
        heroesAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

        heroesRecyclerView.setAdapter(heroesAdapter);
        heroesViewModel.getVmAllHeroesCombined().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> heroesList) {
                // Part 1 of 2.
                // This is needed, if we want the layout to draw itself only after we are done
                // (e.g. have returned from the DetailsFragment via shared views and transitions.
                postponeEnterTransition();


                heroesAdapter.submitList(heroesList);

                // Part 2 of 2.
                // This is needed, if we want the layout to draw itself after we are done
                // Start the transition once all views have been
                // measured and laid out
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

    private void setupProgressBar(View view) {
        // Subscribe to and observe changes happening in the LiveData variable named isLoading
        // and, if true, show the progress bar, otherwise hide it.
        heroesViewModel.getVmIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    view.findViewById(R.id.heroes_progress_bar).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.heroes_progress_bar).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // Don't forget to cancel requests if the Users can't see their response.
    @Override
    public void onDestroyView() {
        boolean requestCancelled = heroesViewModel.cancelRequestWithTag(REQUEST_TAG);
        Log.d(TAG, "onDestroyView: Was request with tag= "
                + REQUEST_TAG + " cancelled? " + requestCancelled);
        super.onDestroyView();
    }
}