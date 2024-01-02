package dev.keero.soften_module.fragments.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.keero.soften_module.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout using DataBindingUtil
        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater, container, false);

        // get the root view from the binding object that is defined in the xml.
        View view = binding.getRoot();

        // logic here

        return view;
    }
}