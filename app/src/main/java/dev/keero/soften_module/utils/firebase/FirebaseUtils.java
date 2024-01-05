package dev.keero.soften_module.utils.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUtils {

    // Private constructor to prevent instantiation
    private FirebaseUtils() {
    }

    public static String getCurrentUserId(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            return null;
        }
        return currentUser.getUid();
    }
}
