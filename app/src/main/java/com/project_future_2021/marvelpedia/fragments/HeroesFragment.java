package com.project_future_2021.marvelpedia.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
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
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.ArrayList;
import java.util.List;

public class HeroesFragment extends Fragment {

    private static final String TAG = "HeroesFragment";
    private static final String REQUEST_TAG = "HeroesFragmentRequest";
    private HeroesViewModel heroesViewModel;
    private MyListAdapter heroesAdapter;
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

    //https://material.io/develop/android/theming/motion
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Transition animation = TransitionInflater.from(requireContext()).inflateTransition(
                android.R.transition.move
        );
        setSharedElementEnterTransition(animation);
        setSharedElementReturnTransition(animation);*/

        /*Explode explode = new Explode();
        requireActivity().getWindow().setExitTransition(explode);*/
        /*setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));*/

        //MaterialFadeThrough exitTransition = new MaterialFadeThrough();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Part 1 of 2.
        /* This is needed, if we want the layout to draw itself only after we are done (e.g. have returned from the DetailsFragment via shared views and transitions.*/
        postponeEnterTransition();
        final ViewGroup parentView = (ViewGroup) view.getParent();

        Log.d(TAG, "onViewCreated: ");


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
        heroesAdapter = new MyListAdapter(new ArrayList<>(), new MyListAdapter.MyClickListener() {
            @Override
            public void onClick(View v, Hero data) {
                //Toast.makeText(getContext(), "O " + data.getName() + "favorite = " + data.getFavorite(), Toast.LENGTH_SHORT).show();
                // goto Details (following instructions specified in nav_graph.xml
                NavDirections action = HeroesFragmentDirections.actionHeroesFragmentToDetailsFragment(data);

                FragmentNavigator.Extras.Builder extrasBuilder = new FragmentNavigator.Extras.Builder();

                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroName), "nameTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroDescription), "descriptionTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedImageViewHeroThumbnail), "thumbnailTN");

                //Navigation.findNavController(view).navigate(action.getActionId(), action.getArguments(), null, extras);
                Navigation.findNavController(view).navigate(action, extrasBuilder.build());
                Log.d(TAG, "onClick: Navigating to DetailsFragment with Hero pressed: " + data.getName());

                /*
                cardView.setOnClickListener{
                        val extras = FragmentNavigatorExtras(imageView to "imageView")
                        findNavController().navigate(R.id.detailAction, null, null, extras)
                    }

                    override fun onCreate(savedInstanceState: Bundle?) {
                            super.onCreate(savedInstanceState)
                               sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
                    }
                    */
            }
        });
        // That should -THEORETICALLY- scroll to the last position of the RecyclerView, but...
        heroesAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        //recyclerView.setAdapter(heroesAdapter);


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

                // Part 2 of 2.
                /* This is needed, if we want the layout to draw itself after we are done*/
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
                //TODO: go here https://stackoverflow.com/questions/53614436/how-to-implement-shared-transition-element-from-recyclerview-item-to-fragment-wi
                //runLayoutAnimation(recyclerView);
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
                    view.findViewById(R.id.progressBarTop).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.progressBarTop).setVisibility(View.INVISIBLE);
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