package dev.keero.soften_module.utils.presenters;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.firebase.callbacks.CartCallBack;

public class CartPresenter {
    private static final String TAG = "CartPresenter";
    private static final String CART_COLLECTION_PATH = "users_cart";
    private static final String BOOK_COLLECTION_PATH = "books";

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

                            Book book = new Book(bookId, bookTitle, bookAuthor, bookType);
                            books.add(book);

                            Log.d(TAG, "Book{id=" + bookId +
                                    ", title='" + bookTitle +
                                    "', author='" + bookAuthor +
                                    "', type='" + bookType + "'}");
                        }

                        callBack.onCartDetailsLoaded(books);
                    } else {
                        Log.w(TAG, "Error getting books.", task.getException());
                    }
                });

    }

}
