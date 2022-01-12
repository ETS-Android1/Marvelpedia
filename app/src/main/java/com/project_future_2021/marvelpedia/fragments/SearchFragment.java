package com.project_future_2021.marvelpedia.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private static final String REQUEST_TAG = "SearchFragmentRequest";
    private HeroesViewModel heroesViewModel;
    private MyListAdapter searchAdapter;
    private EditText userTyped;
    private Button btnSearch;
    private ViewGroup parentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        heroesViewModel = new ViewModelProvider(requireActivity()).get(HeroesViewModel.class);
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        Button btnDelete = view.findViewById(R.id.search_button_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heroesViewModel.deleteAllHeroes();
            }
        });
    }

    private void initViews(View view) {
        parentView = (ViewGroup) view.getParent();

        btnSearch = view.findViewById(R.id.search_button_search);
        userTyped = view.findViewById(R.id.search_edit_text_user_input);

        setupRecyclerView(view);
        setupUserInputEditText();
        setupSearchButton();
    }

    private void setupRecyclerView(View view) {
        RecyclerView searchRecyclerView = view.findViewById(R.id.search_recycler_view);
        searchAdapter = new MyListAdapter(new MyListAdapter.HeroDiff(), new ArrayList<>(), new MyListAdapter.myClickListener() {
            // Handle clicks anywhere on the Hero BUT his favorites icon.
            @Override
            public void onClick(View v, Hero data) {
                // Goto DetailsFragment (following instructions specified in nav_graph.xml)
                NavDirections actionFromSearchToDetails = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(data);

                FragmentNavigator.Extras.Builder extrasBuilder = new FragmentNavigator.Extras.Builder();

                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroName), "nameTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedTextViewHeroDescription), "descriptionTN");
                extrasBuilder.addSharedElement(v.findViewById(R.id.sharedImageViewHeroThumbnail), "thumbnailTN");

                Navigation.findNavController(view).navigate(actionFromSearchToDetails, extrasBuilder.build());
            }

            // Handle clicks on his favorites icon.
            @Override
            public void onFavoritePressed(View v, Hero data, int position) {
                // If the Hero was already favorite (and in our Database subsequently),
                // make him un-favorite and update the Database.
                if (data.getFavorite()) {
                    data.setFavorite(false);
                    heroesViewModel.updateHero(data);
                }
                // If he was not favorite, make him now, and Insert him into the Database.
                // (Insert instead of Update, because he might not already be in the Database,
                // and we have defined that any Insert conflict will be resolved with Replace).
                else {
                    data.setFavorite(true);
                    heroesViewModel.insertHero(data);
                }
                //searchAdapter.notifyItemChanged(position);
            }
        });
        searchRecyclerView.setAdapter(searchAdapter);
        heroesViewModel.getVmListOfHeroesTheUserSearchedFor().observe(getViewLifecycleOwner(), new Observer<List<Hero>>() {
            // What happens when server returns a new list of heroes, or any change in that list happens.
            @Override
            public void onChanged(List<Hero> resultHeroes) {
                postponeEnterTransition();

                // If no heroes were found, let the user know, and also 'empty' the adapter.
                if (resultHeroes.isEmpty()) {
                    Toast.makeText(requireContext(), "No heroes matched your criteria \nTry different name", Toast.LENGTH_LONG).show();
                }
                searchAdapter.submitList(resultHeroes);

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

    // Provide the functionality/logic of the Edit Text in which the users type.
    // We want to make the keyboard action to be 'search' (magnifying glass instead of "Enter button"),
    // and make it so it is equal in functionality with the user just pressing the Search Button.
    private void setupUserInputEditText() {
        userTyped.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btnSearch.callOnClick();
                }
                return true;
            }
        });
    }

    // Provide the functionality/logic of the Search Button.
    // We want to send a call to the Server with the input of the user,
    // and we want to hide the keyboard after users press the Search Button.
    private void setupSearchButton() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            // What happens when user hits the Search button.
            @Override
            public void onClick(View v) {
                // Trim any space characters the user gave.
                String heroName = userTyped.getText().toString().trim();
                if (heroName.isEmpty()) {
                    Toast.makeText(requireContext(), "You did not write anything!", Toast.LENGTH_SHORT).show();
                } else {
                    heroesViewModel.searchForHeroesWithName(heroName, REQUEST_TAG);
                    closeKeyboard();
                }
            }
        });
    }

    // Method for closing the keyboard.
    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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