package dev.keero.soften_module.fragments.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;

import dev.keero.soften_module.adapter.CartAdapter;
import dev.keero.soften_module.databinding.FragmentCartBinding;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.ItemRemovedListener;
import dev.keero.soften_module.utils.firebase.callbacks.CartCallBack;
import dev.keero.soften_module.utils.presenters.CartPresenter;
import dev.keero.soften_module.utils.DialogUtils;
import dev.keero.soften_module.utils.ItemClickListener;
import dev.keero.soften_module.utils.firebase.FirebaseUtils;

public class CartFragment extends Fragment implements ItemClickListener, CartCallBack, ItemRemovedListener {
    private static final String TAG = "CartFragment";
    private FragmentCartBinding binding;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Book> dataSet;
    protected CartAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout using DataBindingUtil
        binding = FragmentCartBinding.inflate(inflater, container, false);

        // get the root view from the binding object that is defined in the xml.

        View view = binding.getRoot();

        //circular
        binding.progressCircular.setVisibility(View.VISIBLE);

        new CartPresenter().loadCart(FirebaseUtils.getCurrentUserId(), this);

        // check out

        binding.checkoutButton.setOnClickListener(v -> {

            // Transfer items to reservation and delete from cart
            new CartPresenter().checkout(FirebaseUtils.getCurrentUserId(), dataSet, this);
            Toast.makeText(requireContext(), "Added Books into reservation.", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onCartDetailsLoaded(ArrayList<Book> books) {
        if(!isAdded()) return; //checks if fragment is attached.

        dataSet = books;
        adapter = new CartAdapter(dataSet);

        // sets the layout manager as linear (since we're only using scrollable list.)
        layoutManager = new LinearLayoutManager(requireContext());

        //set click listener
        adapter.setItemClickListener(this);

        binding.cartRecyclerView.setAdapter(adapter);
        binding.cartRecyclerView.setLayoutManager(layoutManager);

        // Check if the dataset is empty and update the visibility accordingly
        if (dataSet == null || dataSet.isEmpty()) {
            binding.cartRecyclerView.setVisibility(View.GONE);
            binding.cartDefaultView.setVisibility(View.VISIBLE);
            binding.checkoutButton.setVisibility(View.GONE);
        } else {
            binding.cartRecyclerView.setVisibility(View.VISIBLE);
            binding.cartDefaultView.setVisibility(View.GONE);
            binding.checkoutButton.setVisibility(View.VISIBLE);
        }


        binding.progressCircular.setVisibility(View.GONE);
    }

    @Override
    public void onItemClickListener(int position){
        Log.d(TAG, " Clicked " + dataSet.get(position).getId());

        DialogUtils.setItemRemovedListener(this);
        DialogUtils.showDialog(requireContext(), dataSet.get(position), true);
    }

    @Override
    public void onItemRemoved(Book book){
        requireActivity().runOnUiThread(() -> {
            // show loading
            binding.progressCircular.setVisibility(View.VISIBLE);

            // reload cart data
            new CartPresenter().loadCart(FirebaseUtils.getCurrentUserId(), this);
        });
    }

}