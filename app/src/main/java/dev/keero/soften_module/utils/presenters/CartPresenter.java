package dev.keero.soften_module.utils.presenters;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.firebase.callbacks.CartCallBack;

public class CartPresenter {
    private static final String TAG = "CartPresenter";
    private static final String CART_COLLECTION_PATH = "users_cart";
    private static final String BOOK_COLLECTION_PATH = "books";
    private static final String RESERVATION_COLLECTION_PATH = "users_reservation";

    public void checkout(String userId, ArrayList<Book> cartItems, CartCallBack callBack){
        transferItems(userId, cartItems);

        deleteCart(userId, cartItems);

        loadCart(userId, callBack);
    }

    public void loadCart(String userId, final CartCallBack callBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(CART_COLLECTION_PATH)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       DocumentSnapshot userDocument = task.getResult();
                       if(userDocument.exists()) {
                           List<String> bookIds = new ArrayList<>();

                           Object cartObject = userDocument.get("cart");

                           if(cartObject instanceof List<?>) {
                               List<?> rawList = (List<?>) cartObject;

                               for (Object item : rawList) {
                                   if (item instanceof String) {
                                       bookIds.add((String) item);
                                   }
                               }
                           }

                           loadBooks(bookIds, callBack);
                       } else {
                           Log.d(TAG, "User document does not exist");
                           callBack.onCartDetailsLoaded(new ArrayList<>());
                       }
                   } else {
                       Log.w(TAG, "Error getting user document.", task.getException());
                   }
                });

    }

    private void loadBooks(List<String> bookIds, final CartCallBack callBack){
        if(bookIds == null || bookIds.isEmpty()){
            callBack.onCartDetailsLoaded(new ArrayList<>());
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(BOOK_COLLECTION_PATH)
                .whereIn(FieldPath.documentId(), bookIds).get().addOnCompleteListener(task -> {
                    ArrayList<Book> books = new ArrayList<>();
                    if(task.isSuccessful()){

                        for(QueryDocumentSnapshot document : task.getResult()){
                            String bookId = document.getId();
                            String bookTitle = document.getString("bookTitle");
                            String bookAuthor = document.getString("bookAuthor");
                            String bookType = document.getString("bookType");
                            String bookDescription = document.getString("bookDescription");

                            Book book = new Book(bookId, bookTitle, bookAuthor, bookType, bookDescription);
                            books.add(book);

                            Log.d(TAG, "Book{id=" + bookId +
                                    ", title='" + bookTitle +
                                    "', author='" + bookAuthor +
                                    "', type='" + bookType +
                                    "', description='" + bookDescription + "'}");
                        }

                        callBack.onCartDetailsLoaded(books);
                    } else {
                        Log.w(TAG, "Error getting books.", task.getException());
                    }
                });

    }

    private void transferItems(String userId, ArrayList<Book> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if the document for userId exists
        DocumentReference userDocRef = db.collection(RESERVATION_COLLECTION_PATH).document(userId);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document exists, proceed with transferring items
                    performTransfer(userId, cartItems);
                } else {
                    // Document doesn't exist, create one and then transfer items
                    createUserDocument(userId, cartItems);
                }
            } else {
                // Handle the case where document retrieval failed
                Log.e(TAG, "Error checking for document existence for user: " + userId, task.getException());
            }
        });
    }

    private void createUserDocument(String userId, ArrayList<Book> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new document with the specified userId
        db.collection(RESERVATION_COLLECTION_PATH)
                .document(userId)
                .set(new HashMap<>()) // You can customize the initial data if needed
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User document created for userId: " + userId);
                    // Proceed with transferring items after creating the document
                    performTransfer(userId, cartItems);
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error creating user document for userId: " + userId, e));
    }

    private void performTransfer(String userId, ArrayList<Book> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Rest of your existing code for transferring items
        List<String> bookIds = new ArrayList<>();
        for (Book book : cartItems) {
            bookIds.add(book.getId());
        }

        db.collection(RESERVATION_COLLECTION_PATH)
                .document(userId)
                .update("reservation", bookIds)
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "Books transferred to reservation for user: " + userId))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error transferring books to reservation for user: " + userId, e));
    }

    private void deleteCart(String userId, ArrayList<Book> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> bookIdsToDelete = new ArrayList<>();

        for (Book book : cartItems) {
            bookIdsToDelete.add(book.getId());
        }

        // Remove the book IDs from the 'cart' array in the 'users_cart' document
        db.collection(CART_COLLECTION_PATH)
                .document(userId)
                .update("cart", FieldValue.arrayRemove(bookIdsToDelete.toArray()))
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "Books deleted from cart: " + bookIdsToDelete))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error deleting books from cart: " + bookIdsToDelete, e));
    }

}
