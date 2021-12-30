package com.project_future_2021.marvelpedia;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//action view, για να κανεις Share με Implicit Intent στα social media.
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initBottomNavigation();
    }


    private void initBottomNavigation() {
        BottomNavigationView btm_nav_view = findViewById(R.id.main_btm_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.heroesFragment, R.id.favoritesFragment, R.id.searchFragment, R.id.profileFragment/*, R.id.detailsFragment*/)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(btm_nav_view, navController);

        btm_nav_view.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // Actually just do nothing. (did not forget to write some code/logic here)
                Log.d(TAG, "onNavigationItemReselected: Nop");
            }
        });
    }

    // to handle what happens when the user presses the back 'arrow' of the toolbar/actionbar
    /*Attempts to navigate up in the navigation hierarchy.
    Suitable for when the user presses the "Up" button
    marked with a left (or start)-facing arrow in the upper left (or starting) corner of the app UI.*/
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp: ");
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        // navigateUp OR if that fails, call the super()
        return navController.navigateUp() || super.onNavigateUp();
    }
}