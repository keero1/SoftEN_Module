package dev.keero.soften_module;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.keero.soften_module.activities.LoginActivity;
import dev.keero.soften_module.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // trigger splashscreen
        SplashScreen.installSplashScreen(this);

        // return to the main theme.
        setTheme(R.style.Theme_SoftEN_Module_NoActionBar);

        //set binding
        dev.keero.soften_module.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}