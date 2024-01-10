package dev.keero.soften_module.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import dev.keero.soften_module.R;
import dev.keero.soften_module.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    private FirebaseAuth fAuth;
    GoogleSignInClient fGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebase
        fAuth = FirebaseAuth.getInstance();

        //google
        initGoogleSignInClient();

        //set binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.loginEmail.getText().toString().trim();
            String password = binding.loginPassword.getText().toString().trim();

            if(!email.isEmpty() && !password.isEmpty()){
                authenticateUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Forgot password and Google

        binding.loginForgotPassword.setOnClickListener(v -> {
            //qwe
            Toast.makeText(LoginActivity.this, "Not yet implemented.", Toast.LENGTH_SHORT).show();
        });

        binding.googleButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Starting Google sign in..", Toast.LENGTH_SHORT).show();
            signInWithGoogle();
        });


        // Sign Up button

        binding.signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void authenticateUser(String email, String password){
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail:success");
                FirebaseUser user = fAuth.getCurrentUser();
                updateUI(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.w(TAG, "User is null");
        }
    }

    private void signInWithGoogle(){
        Intent intent = fGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try{
                            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                            fAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to sign in" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (ApiException e){
                            e.printStackTrace();
                        }
                    }
                }
    });

    //init

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        fGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

}