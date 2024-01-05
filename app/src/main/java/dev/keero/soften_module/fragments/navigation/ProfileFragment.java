package dev.keero.soften_module.fragments.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import dev.keero.soften_module.activities.LoginActivity;
import dev.keero.soften_module.activities.profile.ReservationActivity;
import dev.keero.soften_module.activities.profile.ReturnActivity;
import dev.keero.soften_module.databinding.FragmentProfileBinding;
import dev.keero.soften_module.utils.DateUtils;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FirebaseAuth fAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //firebase

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = fAuth.getCurrentUser();


        // inflate layout using DataBindingUtil
        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater, container, false);

        // get the root view from the binding object that is defined in the xml.
        View view = binding.getRoot();

        // get the info of user
        assert fUser != null;
        String displayName = fUser.getDisplayName();

        assert fUser.getMetadata() != null;
        String dateString = DateUtils.getDate(fUser.getMetadata().getCreationTimestamp());

        String joinedDate = "Joined since "+ dateString;

        Log.d(TAG, String.valueOf(fUser.getMetadata().getCreationTimestamp()));

        binding.profileName.setText(displayName);
        binding.profileJoinedDate.setText(joinedDate);

        binding.profileSettings.setOnClickListener(v -> {
            fAuth.signOut();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();

            Toast.makeText(requireActivity(), "logged out successfully.", Toast.LENGTH_SHORT).show();
        });

        // buttons

        binding.profileReservation.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ReservationActivity.class);
            startActivity(intent);
        });

        binding.profileReturn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ReturnActivity.class);
            startActivity(intent);
        });

        return view;
    }

}