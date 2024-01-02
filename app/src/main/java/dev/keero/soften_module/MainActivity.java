package dev.keero.soften_module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import dev.keero.soften_module.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // navigation

        setSupportActionBar(binding.mainToolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bottom_nav_home, R.id.bottom_nav_cart, R.id.bottom_nav_profile) //pass all the ids of fragments from nav_graph
                .build();

        // https://stackoverflow.com/a/58859118
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        assert navHostFragment != null; // may produce null
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        visibilityOfNavigationElements(navController);
    }

    private void visibilityOfNavigationElements(NavController navController){
        navController.addOnDestinationChangedListener(( controller, destination, bundle) -> {

            int destinationId = destination.getId();
            if(getSupportActionBar() != null){
                if(destinationId == R.id.bottom_nav_home){
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        });
    }
}