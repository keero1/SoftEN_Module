package dev.keero.soften_module.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import dev.keero.soften_module.R;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.model.Cart;
import dev.keero.soften_module.model.Reservation;
import dev.keero.soften_module.utils.firebase.FirebaseUtils;

public class DialogUtils {
    private static final String TAG = "DialogUtils";
    private static final String CART_COLLECTION_PATH = "users_cart";
    private static final String RESERVATION_COLLECTION_PATH = "users_reservation";
    private static DocumentReference userCartRef;
    private static Dialog dialog;

    // remove listener
    private static ItemRemovedListener itemRemovedListener;

    public static void setItemRemovedListener(ItemRemovedListener listener) {
        itemRemovedListener = listener;
    }

    public DialogUtils() {
        // Required empty constructor
    }

    private static void initializeDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(FirebaseUtils.getCurrentUserId() == null){
            return;
        }
        userCartRef = db.collection(CART_COLLECTION_PATH).document(Objects.requireNonNull(FirebaseUtils.getCurrentUserId()));
    }

    public static void showDialog(Context context, Book book, boolean isCart){
        initializeDB();
        setupDialog(context, book, isCart);
        dialog.show();
    }

    public static void showDialog(Context context, Book book) {
        initializeDB();
        setupDialog(context, book);
        dialog.show();
    }

    // dialog setup.
    private static void setupDialog(Context context, Book book, boolean isCart){
        dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(R.layout.book_dialogue_layout);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_view);
        }

        setBookDetails(book);
        setButtons(context, book, isCart);
    }

    private static void setupDialog(Context context, Book book){
        dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(R.layout.book_dialogue_layout);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_view);
        }

        setBookDetails(book);
        setButtons();
    }

    private static void setBookDetails(Book book){
        TextView bookTitle = dialog.findViewById(R.id.book_title);
        TextView bookAuthor = dialog.findViewById(R.id.book_author);
        TextView bookDescription = dialog.findViewById(R.id.book_description);

        bookTitle.setText(book.getBookName());
        bookAuthor.setText(book.getBookAuthor());
        bookDescription.setText(book.getBookDescription());
    }

    private static void setButtons(Context context, Book book, boolean isCart){
        TextView add = dialog.findViewById(R.id.add_book);
        TextView cancel = dialog.findViewById(R.id.cancel_book);

        cancel.setOnClickListener(v -> dialog.dismiss());

        if(isCart){
            add.setText(R.string.cart_remove_item_text);
            add.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context.getApplicationContext(), R.color.orange_day)));
        }

        add.setOnClickListener(v -> handleCart(context, book, isCart));
    }

    private static void setButtons(){
        TextView cancel = dialog.findViewById(R.id.cancel_book);
        TextView add = dialog.findViewById(R.id.add_book);

        add.setVisibility(View.GONE);

        // Set the gravity for cancel_book within the LinearLayout
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                300,  // Set width to 150dp
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cancelParams.gravity = Gravity.CENTER_HORIZONTAL;  // Center horizontally
        cancel.setLayoutParams(cancelParams);

        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    // ----------- CART HANDLE -------------- //

    private static void handleCart(Context context, Book book, boolean isCart) {
        if(userCartRef == null){
            Log.d(TAG, "User ID does not exist");
            return;
        }

        userCartRef.get().addOnSuccessListener(documentSnapshot -> {
            if(!documentSnapshot.exists()){
                createNewCart(book, context);
                return;
            }

            if(isCart) {
                removeItem(documentSnapshot, book, context);
                return;
            }
            hasItemInReservation(documentSnapshot, context, book);
        });
    }

    // check if reservation has any item.
    private static void hasItemInReservation(DocumentSnapshot cartDocumentSnapshot, Context context, Book book) {
        DocumentReference usersReservationRef = FirebaseFirestore.getInstance().collection(RESERVATION_COLLECTION_PATH).document(Objects.requireNonNull(FirebaseUtils.getCurrentUserId()));

        usersReservationRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Check if there are items in the reservation
                Reservation reservation = documentSnapshot.toObject(Reservation.class);
                if (reservation != null && reservation.getReservation() != null && !reservation.getReservation().isEmpty()) {
                    // Inform the user that there are items in the reservation
                    addToCart(cartDocumentSnapshot, book, context);
                } else {
                    // No items in reservation, proceed with addToCart
                    Toast.makeText(context, "You have items in the reservation. You cannot add books to cart.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where the reservation document does not exist
                addToCart(cartDocumentSnapshot, book, context);
            }
        });
    }


    private static void addToCart(DocumentSnapshot documentSnapshot, Book book, Context context) {
        Cart cart = documentSnapshot.toObject(Cart.class);
        ArrayList<String> currentCart = (cart != null) ? cart.getCart() : new ArrayList<>();

        if(currentCart.contains(book.getId())) {
            Toast.makeText(context, "This book is already in your Cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(currentCart.size() >= 4){
            Toast.makeText(context, "You can only add a maximum of 4 books into the cart.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Cart is Full.");
            return;
        }

        currentCart.add(book.getId());

        userCartRef.update("cart", currentCart)
                .addOnSuccessListener(v -> {
                    dialog.dismiss();
                    Toast.makeText(context, "Book has been added to cart.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Log.d(TAG, "Error adding Item to Cart.");
                });
    }

    private static void createNewCart(Book book, Context context){
        ArrayList<String> newCart =  new ArrayList<>();
        newCart.add(book.getId());
        userCartRef.set(new HashMap<String, Object>() {{
            put("cart", newCart);
            Toast.makeText(context, "Book has been added to cart.", Toast.LENGTH_SHORT).show();
        }}).addOnSuccessListener(v -> dialog.dismiss()).addOnFailureListener(e -> {
            dialog.dismiss();
            Log.d(TAG, "Error creating new Cart.");
        });
    }

    // remove item

    private static void removeItem(DocumentSnapshot documentSnapshot, Book book, Context context){
        Cart cart = documentSnapshot.toObject(Cart.class);
        ArrayList<String> currentCart = (cart != null) ? cart.getCart() : new ArrayList<>();

        if(currentCart.contains(book.getId())){
            currentCart.remove(book.getId());
                userCartRef.update("cart", currentCart)
                        .addOnSuccessListener(v -> {
                            dialog.dismiss();
                            Toast.makeText(context, "Book has been removed from cart.", Toast.LENGTH_SHORT).show();

                            // refresh fragment
                            if(itemRemovedListener != null){
                                itemRemovedListener.onItemRemoved(book);
                            }
                        })
                        .addOnFailureListener(e -> {
                            dialog.dismiss();
                            Log.d(TAG, "Error removing Item from Cart.");
                        });
        } else {
            Log.d(TAG, "Trying to remove an item that does not exist in Cart.");
        }
    }

}
