package com.project_future_2021.marvelpedia.fragments;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Explode explode = new Explode();
        requireActivity().getWindow().setExitTransition(explode);*/

        // Have an animation for your shared-views, when the enter, and return.
        Transition animation = TransitionInflater.from(requireContext()).inflateTransition(
                android.R.transition.move
        );
        setSharedElementEnterTransition(animation);
        setSharedElementReturnTransition(animation);


        /*setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the arguments from sender-Fragment
        DetailsFragmentArgs argsFromHeroesFragment = DetailsFragmentArgs.fromBundle(getArguments());
        Hero heroToShowDetailsFor = argsFromHeroesFragment.getHeroForDetailsFragment();

        TextView details_txt = view.findViewById(R.id.sharedTextViewHeroDescription);
        TextView name_txt = view.findViewById(R.id.sharedTextViewHeroName);
        ImageView thumbnail = view.findViewById(R.id.sharedImageViewHeroThumbnail);

        name_txt.setText(heroToShowDetailsFor.getName());
        details_txt.setText(heroToShowDetailsFor.getDescription());

        Glide.with(this)
                .load(heroToShowDetailsFor.getThumbnail().makeImageWithVariant("portrait_xlarge"))
                //.placeholder(R.drawable.ic_baseline_image_search_24)
                .into(thumbnail);

        //make the bottom navigation bar invisible
        requireActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        //make the bottom navigation bar visible again
        requireActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.VISIBLE);
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }
}