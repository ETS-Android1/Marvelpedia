package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.List;

public class HeroesFragment extends Fragment {

    private static final String TAG = "HeroesFragment";
    private static final String REQUEST_TAG = "HeroesFragmentRequest";
    private HeroesViewModel heroesViewModel;

    public static HeroesFragment newInstance() {
        return new HeroesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //"heroesViewModel" will attach(-live) to the getActivity()(i.e. MainActivity)'s lifecycle.
        // So it will survive HeroesFragment destruction when navigating to other fragments.
        heroesViewModel = new ViewModelProvider(/*this*/getActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.heroes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        String Url = heroesViewModel.createUrlforApiCall(request_type);

        TextView test_textView = view.findViewById(R.id.test_textView);
        heroesViewModel.getHeroesFromServer(Url, REQUEST_TAG);
        heroesViewModel.getLiveDataHeroesList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> heroesList) {
                test_textView.setText(heroesList.toString());
            }
        });

        Log.d(TAG, "onViewCreated: mUrl is: " + Url);

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