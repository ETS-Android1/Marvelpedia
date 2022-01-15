package com.project_future_2021.marvelpedia.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";

    Hero heroToShowDetailsFor;
    ConstraintLayout details_base_layout;
    TextView details_textview_hero_description;
    TextView details_textview_hero_name;
    ImageView details_imageview_hero_thumbnail;

    /* The other possible swatches:
        private Palette.Swatch vibrantSwatch;
        private Palette.Swatch lightVibrantSwatch;
        private Palette.Swatch darkVibrantSwatch;
        private Palette.Swatch lightMutedSwatch;
        private Palette.Swatch darkMutedSwatch;
    */
    // We went with this one, just personal preference.
    private Palette.Swatch mutedSwatch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Have an animation for our shared-views, when they enter, and return.
        Transition animation = TransitionInflater.from(requireContext()).inflateTransition(
                android.R.transition.move
        );
        setSharedElementEnterTransition(animation);
        setSharedElementReturnTransition(animation);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideNavigationBar();

        receiveArgumentsFromSenderFragment();

        initViews(view);
    }

    private void hideNavigationBar() {
        // Make the bottom navigation bar invisible.
        requireActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.GONE);
    }

    private void receiveArgumentsFromSenderFragment() {
        // Get the arguments from sender-Fragment.
        DetailsFragmentArgs argsFromHeroesFragment = DetailsFragmentArgs.fromBundle(getArguments());
        heroToShowDetailsFor = argsFromHeroesFragment.getHeroForDetailsFragment();
    }

    private void initViews(View view) {
        details_base_layout = view.findViewById(R.id.details_base_layout);
        details_textview_hero_name = view.findViewById(R.id.sharedTextViewHeroName);
        details_textview_hero_description = view.findViewById(R.id.sharedTextViewHeroDescription);
        details_imageview_hero_thumbnail = view.findViewById(R.id.sharedImageViewHeroThumbnail);

        details_textview_hero_name.setText(heroToShowDetailsFor.getName());
        details_textview_hero_description.setText(heroToShowDetailsFor.getDescription());

        // Load the image with the help of Glide
        Glide.with(this)
                .asBitmap()
                .load(heroToShowDetailsFor.getThumbnail().makeImageWithVariant("portrait_xlarge"))
                .into(new BitmapImageViewTarget(details_imageview_hero_thumbnail) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);

                        // Create the palette Asynchronously, with 64 colors (64 is actually a lot, usually 32 or even 16 is enough...)
                        Palette.generateAsync(bitmap, 64, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                // If the palette successfully "found" colors from the thumbnail Image.
                                if (palette != null) {
                                    /*
                                        vibrantSwatch = palette.getVibrantSwatch();
                                        lightVibrantSwatch = palette.getLightVibrantSwatch();
                                        darkVibrantSwatch = palette.getDarkVibrantSwatch();
                                        lightMutedSwatch = palette.getLightMutedSwatch();
                                        darkMutedSwatch = palette.getDarkMutedSwatch();
                                    */
                                    mutedSwatch = palette.getMutedSwatch();

                                    // If the palette successfully "found" colors from the thumbnail Image for the mutedSwatch variation.
                                    if (mutedSwatch != null) {

                                        // Change the base layout background color.
                                        details_base_layout.setBackgroundColor(mutedSwatch.getRgb());
                                        // Change the hero name text color.
                                        details_textview_hero_name.setTextColor(mutedSwatch.getTitleTextColor());
                                        // Change the hero description text color.
                                        details_textview_hero_description.setTextColor(mutedSwatch.getTitleTextColor());

                                        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
                                        if (actionBar != null) {
                                            actionBar.setDisplayHomeAsUpEnabled(true);
                                            // Change the color of the Action bar.
                                            actionBar.setBackgroundDrawable(new ColorDrawable(manipulateColor(mutedSwatch.getRgb(), 0.62f)));

                                            // Change the color of the Status bar.
                                            requireActivity().getWindow().setStatusBarColor(manipulateColor(mutedSwatch.getRgb(), 0.32f));
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
    }

    // Create a slight variation of the given color.
    private static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    @Override
    public void onDestroyView() {
        // Make the bottom navigation bar visible again.
        requireActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
}