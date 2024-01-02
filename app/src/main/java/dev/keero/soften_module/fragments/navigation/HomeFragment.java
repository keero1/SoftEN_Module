package dev.keero.soften_module.fragments.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import dev.keero.soften_module.R;
import dev.keero.soften_module.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout using DataBindingUtil
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        // get the root view from the binding object that is defined in the xml.
        View view = binding.getRoot();

        // logic here

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                 menuInflater.inflate(R.menu.menu_search, menu);

                MenuItem item = menu.findItem(R.id.action_search);
                SearchView search = (SearchView) item.getActionView();

                assert search != null;
                search.setQueryHint(getString(R.string.appbar_search_hint));

                // Add option Menu Here

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                // Handle option Menu Here
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return view;
    }

}