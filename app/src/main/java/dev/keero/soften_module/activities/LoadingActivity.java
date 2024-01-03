package dev.keero.soften_module.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import dev.keero.soften_module.MainActivity;
import dev.keero.soften_module.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(() -> {
            Toast.makeText(LoadingActivity.this, "Authenticated successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // Adjust the delay as needed
    }
}