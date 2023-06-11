package com.mfc.celiacare;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mfc.celiacare.databinding.ActivityMainBinding;
import com.mfc.celiacare.ui.places.PlacesFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //private PlacesFragment placesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_places, R.id.navigation_news, R.id.navigation_login, R.id.navigation_account, R.id.navigation_map, R.id.navigation_places_scrolling, R.id.navigation_news_scrolling, R.id.navigation_my_places, R.id.navigation_my_places)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        /*placesFragment = new PlacesFragment();
        placesFragment.loadPlacesImages();*/
    }

}