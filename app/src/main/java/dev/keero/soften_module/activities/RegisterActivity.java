package dev.keero.soften_module.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import dev.keero.soften_module.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    ActivityRegisterBinding binding;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebase
        fAuth = FirebaseAuth.getInstance();

        //set binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.registerEmail.getText().toString().trim();
            String displayName = binding.registerDisplayName.getText().toString().trim();
            String password = binding.registerPassword.getText().toString().trim();

            if(!email.isEmpty() && !displayName.isEmpty() && !password.isEmpty()){
                createAccount(email, displayName, password);
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });




        //OnBackPressed is deprecated...
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void createAccount(String email, String displayName, String password){
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = fAuth.getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();

                updateUI(user, profileUpdates);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
            }
        });
    }

    private void updateUI(FirebaseUser user, UserProfileChangeRequest profileUpdates){

        if(user != null){
            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Log.d(TAG, "User display name updated.");

                    // so we wouldn't save the user. we want them to input their registered account again.
                    FirebaseAuth.getInstance().signOut();

                    finish();
                } else {
                    Log.w(TAG, "Failed to update user display name.", task.getException());
                }
            });
        } else {
            Log.w(TAG, "User is null");
        }
    }
}