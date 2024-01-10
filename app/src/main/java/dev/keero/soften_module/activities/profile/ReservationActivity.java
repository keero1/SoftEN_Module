package dev.keero.soften_module.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

import dev.keero.soften_module.adapter.ReservationAdapter;
import dev.keero.soften_module.databinding.ActivityReservationBinding;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.DialogUtils;
import dev.keero.soften_module.utils.ItemClickListener;
import dev.keero.soften_module.utils.firebase.FirebaseUtils;
import dev.keero.soften_module.utils.firebase.callbacks.ReservationCallBack;
import dev.keero.soften_module.utils.presenters.ReservationPresenter;

public class ReservationActivity extends AppCompatActivity implements ItemClickListener, ReservationCallBack {
    private static final String TAG = "ReservationActivity";
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Book> dataSet;
    protected ReservationAdapter adapter;
    private ActivityReservationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //set binding
        binding = ActivityReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressCircular.setVisibility(View.VISIBLE);

        new ReservationPresenter().loadCart(FirebaseUtils.getCurrentUserId(), this);
    }
    @Override
    public void onReservationDetailsLoaded(ArrayList<Book> books) {
        dataSet = books;
        adapter = new ReservationAdapter(dataSet);

        // sets the layout manager as linear (since we're only using scrollable list.)
        layoutManager = new LinearLayoutManager(this);

        //set click listener
        adapter.setItemClickListener(this);

        binding.reservationRecyclerview.setAdapter(adapter);
        binding.reservationRecyclerview.setLayoutManager(layoutManager);

        // Check if the dataset is empty and update the visibility accordingly
        if (dataSet == null || dataSet.isEmpty()) {
            binding.reservationRecyclerview.setVisibility(View.GONE);
            binding.reservationDefaultView.setVisibility(View.VISIBLE);
        } else {
            binding.reservationRecyclerview.setVisibility(View.VISIBLE);
            binding.reservationDefaultView.setVisibility(View.GONE);
        }

        binding.progressCircular.setVisibility(View.GONE);
    }

    @Override
    public void onItemClickListener(int position){
        Log.d(TAG, " Clicked " + dataSet.get(position).getId());

        DialogUtils.showDialog(this, dataSet.get(position));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.d(TAG, "Reservation Activity destroyed.");
    }
}