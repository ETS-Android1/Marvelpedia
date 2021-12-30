package com.project_future_2021.marvelpedia.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.recycler_view.TestListAdapter;
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.ArrayList;
import java.util.List;

public class HeroesFragment extends Fragment {

    private static final String TAG = "HeroesFragment";
    private static final String REQUEST_TAG = "HeroesFragmentRequest";
    private HeroesViewModel heroesViewModel;
    private TestListAdapter heroesAdapter;
    private RecyclerView recyclerView;

    public static HeroesFragment newInstance() {
        return new HeroesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        //"heroesViewModel" will attach(-live) to the getActivity()(i.e. MainActivity)'s lifecycle.
        // So it will survive HeroesFragment destruction when navigating to other fragments.
        heroesViewModel = new ViewModelProvider(/*this*/requireActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.heroes_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");

        // when the user taps on the button, go to the DetailsFragment
        view.findViewById(R.id.heroes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // goto Details (following instructions specified in nav_graph.xml
                NavDirections action = HeroesFragmentDirections.actionHeroesFragmentToDetailsFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        String request_type = "/v1/public/characters";
        String Url = heroesViewModel.createUrlForApiCall(request_type);
        heroesViewModel.getHeroesFromServer(Url, REQUEST_TAG);

        recyclerView = view.findViewById(R.id.recyclerView);
        NestedScrollView nestedScrollView = view.findViewById(R.id.heroes_layout);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    Log.d(TAG, "onScrollChange: We reached the bottom of the page. Will fetch more data now...");
                    heroesViewModel.loadMore(request_type);
                }
            }
        });

        /* option 1:
        // start of option 1
        heroesViewModel.getLiveDataHeroesList().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> heroesList) {
                heroesAdapter = new MyAdapter(heroesList, new MyAdapter.onItemClickListener() {
                    @Override
                    public void onClick(View v, Hero data) {
                        Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(heroesAdapter);
            }
        });
        // end of option 1
        */

        /* option 2:
        // start of option 2
        */
        heroesAdapter = new TestListAdapter(new ArrayList<>(), new TestListAdapter.myTestClickListener() {
            @Override
            public void onClick(View v, Hero data) {
                Toast.makeText(getContext(), "O " + data.getName() + "favorite = " + data.getFavorite(), Toast.LENGTH_SHORT).show();
            }
        });
        // That should -THEORETICALLY- scroll to the last position of the RecyclerView, but...
        heroesAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        //recyclerView.setAdapter(heroesAdapter);


        // TODO: probably wrong, but why does it not work as it should??
        // If we put "recyclerView.setAdapter(heroesAdapter);" only before or after the "observe the list code",
        // the list doesn't show when it's recreated (the user swapped fragments etc)
        // so we put it inside to code block too...
        heroesViewModel.getLiveDataHeroesList().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            @Override
            public void onChanged(List<Hero> heroesList) {
                heroesAdapter.submitList(heroesList);
                recyclerView.setAdapter(heroesAdapter);
                Log.d(TAG, "onChanged: new heroesList is:" + heroesList);
                for (Hero hero : heroesList) {
                    Log.d(TAG, "onChanged: Hero " + hero.getName() + " is favorite? " + hero.getFavorite());
                }
            }
        });
        /*
        // end of option 2
        */

        // subscribe to and observe to changes happening in the livedata variable named isLoading
        // and, if true, show the progress bar, otherwise hide it.
        heroesViewModel.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    // don't forget to cancel requests if the User can't see the response
    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        //if (jsonObjectRequest != null) {
        if (VolleySingleton.getInstance(getContext()) != null) {
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll(REQUEST_TAG);
            Log.d(TAG, "onDestroyView: cancelled request with tag: " + REQUEST_TAG);
        }
        //}
        super.onDestroyView();
    }

}