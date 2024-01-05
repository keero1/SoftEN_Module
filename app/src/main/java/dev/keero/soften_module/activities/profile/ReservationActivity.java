package dev.keero.soften_module.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import dev.keero.soften_module.databinding.ActivityReservationBinding;

public class ReservationActivity extends AppCompatActivity {
    private static final String TAG = "ReservationActivity";
    private ActivityReservationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //set binding
        binding = ActivityReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.d(TAG, "Reservation Activity destroyed.");
    }
}