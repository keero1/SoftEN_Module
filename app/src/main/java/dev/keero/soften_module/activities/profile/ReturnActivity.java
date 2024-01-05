package dev.keero.soften_module.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import dev.keero.soften_module.R;

public class ReturnActivity extends AppCompatActivity {
    private static final String TAG = "ReturnActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Return Activity Destroyed.");
    }
}