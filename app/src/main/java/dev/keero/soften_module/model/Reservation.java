package dev.keero.soften_module.model;

import java.util.ArrayList;

public class Reservation {
    private ArrayList<String> cart;

    public Reservation() {
        // Required empty public constructor for FireStore
    }

    public ArrayList<String> getReservation() {
        return cart;
    }
}
