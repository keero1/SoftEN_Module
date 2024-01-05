package dev.keero.soften_module.fragments.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Objects;

import dev.keero.soften_module.R;
import dev.keero.soften_module.adapter.BookAdapter;
import dev.keero.soften_module.databinding.FragmentHomeBinding;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.presenters.BookPresenter;
import dev.keero.soften_module.utils.DialogUtils;
import dev.keero.soften_module.utils.firebase.callbacks.BookCallBack;
import dev.keero.soften_module.utils.ItemClickListener;

public class HomeFragment extends Fragment implements ItemClickListener, BookCallBack {
    private static final String TAG = "HomeFragment";
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Book> dataSet;
    protected ArrayList<Integer> originalPosition; // we will store the original position before we filter the list.
    protected BookAdapter adapter;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout using DataBindingUtil
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // get the root view from the binding object that is defined in the xml.
        View view = binding.getRoot();

        // initialize data
        new BookPresenter().loadBooks(this);

        // Setup the Search
        setupProvider();


        return view;
    }

    private void setupProvider(){
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu);

                MenuItem item = menu.findItem(R.id.action_search);
                SearchView search = (SearchView) item.getActionView();

                Objects.requireNonNull(search).setQueryHint(getString(R.string.appbar_search_hint));

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filter(newText);
                        return false;
                    }
                });

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Handle option Menu Here
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void filter(String text) {

        // initialize the original position list if it is null, clear if it contains anything.
        if(originalPosition == null){
            originalPosition = new ArrayList<>();
        } else {
            originalPosition.clear();
        }

        ArrayList<Book> filteredList = new ArrayList<>();

        for (int i = 0; i < dataSet.size(); i++) {
            Book item = dataSet.get(i);
            if (item.getBookName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
                originalPosition.add(i);
            }
        }
        if (filteredList.isEmpty()) {
            Log.d(TAG, " No data found...");
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void onBooksLoaded(ArrayList<Book> books){
        if(!isAdded()) return; //checks if fragment is attached.

        dataSet = books;
        adapter = new BookAdapter(dataSet);

        // sets the layout manager as linear (since we're only using scrollable list.)
        layoutManager = new LinearLayoutManager(requireContext());

        //set click listener
        adapter.setItemClickListener(this);

        binding.bookRecyclerView.setAdapter(adapter);
        binding.bookRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClickListener(int position){
        int actualPosition = ((originalPosition != null && originalPosition.size() > position )? originalPosition.get(position) : position);

        Log.d(TAG, " Clicked " + dataSet.get(actualPosition).getId());

        DialogUtils.showDialog(requireContext(), dataSet.get(actualPosition), false);
    }
}