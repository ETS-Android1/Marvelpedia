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

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.viewmodels.DetailsViewModel;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private DetailsViewModel detailsViewModel;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        return inflater.inflate(R.layout.details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView details_txt = view.findViewById(R.id.details_txt);
        detailsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                details_txt.setText(s);
            }
        });

        //make the bottom navigation bar invisible
        getActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        //make the bottom navigation bar visible again
        getActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.VISIBLE);
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }
}