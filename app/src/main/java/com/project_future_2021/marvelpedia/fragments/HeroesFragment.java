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

import com.android.volley.toolbox.JsonObjectRequest;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;
import com.project_future_2021.marvelpedia.viewmodels.HeroesViewModel;

import java.util.List;

public class HeroesFragment extends Fragment {

    private static final String TAG = "HeroesFragment";
    private static final String REQUEST_TAG = "HeroesFragmentRequest";
    private HeroesViewModel heroesViewModel;
    JsonObjectRequest jsonObjectRequest;

    public static HeroesFragment newInstance() {
        return new HeroesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        heroesViewModel = new ViewModelProvider(this).get(HeroesViewModel.class);
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

        TextView heroes_txt = view.findViewById(R.id.heroes_txt);
        heroesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                heroes_txt.setText(s);
            }
        });

        String BASE_URL = getString(R.string.base_url);
        String request_type = "/v1/public/characters";
        String PRIVATE_API_KEY = getString(R.string.private_API_key);
        String PUBLIC_API_KEY = getString(R.string.public_API_key);
        String Url = heroesViewModel.createUrlforApiCall(BASE_URL, request_type, PRIVATE_API_KEY, PUBLIC_API_KEY);

        TextView test_textView = view.findViewById(R.id.test_textView);
        heroesViewModel.getHeroes(requireContext(), Url, REQUEST_TAG);
        heroesViewModel.getmList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> heroesList) {
                test_textView.setText(heroesList.toString());
            }
        });
        //doCall(getContext(), Url);


        Log.d(TAG, "onViewCreated: mUrl is: " + Url);

    }


    // don't forget to cancel requests if the User can't see the response
    @Override
    public void onDestroyView() {
        if (jsonObjectRequest != null) {
            VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll(REQUEST_TAG);
            Log.d(TAG, "onDestroyView: cancelled request with tag: " + REQUEST_TAG);
        }
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

}