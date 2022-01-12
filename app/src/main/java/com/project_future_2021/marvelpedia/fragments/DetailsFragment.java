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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.viewmodels.DetailsViewModel;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private DetailsViewModel detailsViewModel;

    private Palette.Swatch vibrantSwatch;
    private Palette.Swatch lightVibrantSwatch;
    private Palette.Swatch darkVibrantSwatch;
    private Palette.Swatch mutedSwatch;
    private Palette.Swatch lightMutedSwatch;
    private Palette.Swatch darkMutedSwatch;
    TextView textView;

    ConstraintLayout constraintLayout;
    TextView details_txt;
    TextView name_txt;

    private int swatchNumber;

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

        /*TextView */
        details_txt = view.findViewById(R.id.sharedTextViewHeroDescription);
        /*TextView */
        name_txt = view.findViewById(R.id.sharedTextViewHeroName);
        ImageView thumbnail = view.findViewById(R.id.sharedImageViewHeroThumbnail);

        name_txt.setText(heroToShowDetailsFor.getName());
        details_txt.setText(heroToShowDetailsFor.getDescription());

        /*Glide.with(this)
                .load(heroToShowDetailsFor.getThumbnail().makeImageWithVariant("portrait_xlarge"))
                //.placeholder(R.drawable.ic_baseline_image_search_24)
                .into(thumbnail);*/

        //make the bottom navigation bar invisible
        requireActivity().findViewById(R.id.main_btm_nav_view).setVisibility(View.GONE);

        /*textView = view.findViewById(R.id.textView);
        Button btnSwatch = view.findViewById(R.id.buttonSwatch);*/
        constraintLayout = view.findViewById(R.id.details_base_layout);
        /*btnSwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSwatch(v);
            }
        });*/
        //ImageView myImage = view.findViewById(R.id.detailsMyImage);
        //Bitmap bitmap = ((BitmapDrawable) myImage.getDrawable()).getBitmap();
        //Bitmap bitmap = ((BitmapDrawable) myImage.getDrawable()).getBitmap();
        //Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(heroToShowDetailsFor.getThumbnail().makeImageWithVariant("portrait_xlarge")));


        Glide.with(this)
                .asBitmap()
                .load(heroToShowDetailsFor.getThumbnail().makeImageWithVariant("portrait_xlarge"))
                .into(new BitmapImageViewTarget(thumbnail) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap mbitmap, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        super.onResourceReady(mbitmap, transition);

                        Palette.generateAsync(mbitmap, 64, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                if (palette != null) {
                                    vibrantSwatch = palette.getVibrantSwatch();
                                    lightVibrantSwatch = palette.getLightVibrantSwatch();
                                    darkVibrantSwatch = palette.getDarkVibrantSwatch();
                                    mutedSwatch = palette.getMutedSwatch();
                                    lightMutedSwatch = palette.getLightMutedSwatch();
                                    darkMutedSwatch = palette.getDarkMutedSwatch();


//here we set the background color
                                    if (mutedSwatch != null) {
                                        constraintLayout.setBackgroundColor(mutedSwatch.getRgb());
                                        name_txt.setTextColor(mutedSwatch.getTitleTextColor());
                                        details_txt.setTextColor(mutedSwatch.getTitleTextColor());

                                        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
                                        if (actionBar != null) {
                                            actionBar.setDisplayHomeAsUpEnabled(true);
                                            //set color action bar
                                            actionBar.setBackgroundDrawable(new ColorDrawable(manipulateColor(mutedSwatch.getRgb(), 0.62f)));

                                            //set color status bar
                                            requireActivity().getWindow().setStatusBarColor(manipulateColor(mutedSwatch.getRgb(), 0.32f));
                                        }
                                    }
                                }
                            }
                        });
                    }
                });






        /*Palette.from(bitmap)*//*.maximumColorCount(16)*//*.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                if (palette != null) {
                    vibrantSwatch = palette.getVibrantSwatch();
                    lightVibrantSwatch = palette.getLightVibrantSwatch();
                    darkVibrantSwatch = palette.getDarkVibrantSwatch();
                    mutedSwatch = palette.getMutedSwatch();
                    lightMutedSwatch = palette.getLightMutedSwatch();
                    darkMutedSwatch = palette.getDarkMutedSwatch();
                }
            }
        });*/
    }

    public void nextSwatch(View v) {
        Palette.Swatch currentSwatch = null;
        switch (swatchNumber) {
            case 0:
                currentSwatch = vibrantSwatch;
                textView.setText("vibrantSwatch");
                break;
            case 1:
                currentSwatch = lightVibrantSwatch;
                textView.setText("lightVibrantSwatch");
                break;
            case 2:
                currentSwatch = darkVibrantSwatch;
                textView.setText("darkVibrantSwatch");
                break;
            case 3:
                currentSwatch = mutedSwatch;
                textView.setText("mutedSwatch");
                break;
            case 4:
                currentSwatch = lightMutedSwatch;
                textView.setText("lightMutedSwatch");
                break;
            case 5:
                currentSwatch = darkMutedSwatch;
                textView.setText("darkMutedSwatch");
                break;
        }
        if (currentSwatch != null) {
            //constraintLayout.setBackgroundColor(currentSwatch.getRgb());
            //textView.setTextColor(currentSwatch.getTitleTextColor());

            constraintLayout.setBackgroundColor(currentSwatch.getRgb());
            name_txt.setTextColor(currentSwatch.getTitleTextColor());
            details_txt.setTextColor(currentSwatch.getTitleTextColor());


            //ActionBar actionBar = requireActivity().getActionBar();

            ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                //set color action bar
                actionBar.setBackgroundDrawable(new ColorDrawable(manipulateColor(currentSwatch.getRgb(), 0.62f)));

                //set color status bar
                requireActivity().getWindow().setStatusBarColor(manipulateColor(currentSwatch.getRgb(), 0.32f));
            }
        } else {
            //constraintLayout.setBackgroundColor(Color.WHITE);
            //textView.setTextColor(Color.RED);
            constraintLayout.setBackgroundColor(Color.WHITE);
            name_txt.setTextColor(Color.RED);
            details_txt.setTextColor(Color.RED);
        }

        if (swatchNumber < 5) {
            swatchNumber++;
        } else {
            swatchNumber = 0;
        }
    }

    public static int manipulateColor(int color, float factor) {
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