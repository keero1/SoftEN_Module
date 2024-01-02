package dev.keero.soften_module.fragments.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import dev.keero.soften_module.R;
import dev.keero.soften_module.adapter.BookAdapter;
import dev.keero.soften_module.databinding.FragmentHomeBinding;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.BookItemClickListener;
import dev.keero.soften_module.utils.BookPresenter;

public class HomeFragment extends Fragment implements BookItemClickListener {
    private static final String TAG = "HomeFragment";
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Book> dataSet;
    protected BookAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout using DataBindingUtil
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        // get the root view from the binding object that is defined in the xml.
        View view = binding.getRoot();

        //initialize data
        BookPresenter presenter = new BookPresenter();

        //fill the list
        dataSet = presenter.loadBook(new ArrayList<>());

        // sets the layout manager as linear (since we're only using scrollable list.)
        layoutManager = new LinearLayoutManager(requireContext());
        adapter = new BookAdapter(dataSet);

        //set click listener
        adapter.setBookItemClickListener(this);

        binding.bookRecyclerView.setAdapter(adapter);
        binding.bookRecyclerView.setLayoutManager(layoutManager);

        // search
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

                assert search != null;
                search.setQueryHint(getString(R.string.appbar_search_hint));

                // Add option Menu Here

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

    @Override
    public void onBookItemClickListener(int position){
        Log.d(TAG, " Clicked " + position);
    }


    //FILTER

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Book> filteredlist = new ArrayList<Book>();

        // running a for loop to compare elements.
        for (Book item : dataSet) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getBookName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Log.d(TAG, " No data found...");
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

}