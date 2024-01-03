package dev.keero.soften_module.utils;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import dev.keero.soften_module.model.Book;
public class BookPresenter {
    private static final String TAG = "BookPresenter";
    private static final String COLLECTION_PATH = "books";

    public void loadBooks(final FirestoreCallBack callBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(COLLECTION_PATH).orderBy("bookTitle").get().addOnCompleteListener(task -> {
            ArrayList<Book> books = new ArrayList<>();
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    String bookId = document.getId();
                    String bookTitle = document.getString("bookTitle");
                    String bookAuthor = document.getString("bookAuthor");
                    String bookType = document.getString("bookType");

                    Book book = new Book(bookId, bookTitle, bookAuthor, bookType);
                    books.add(book);
                }

                if(callBack != null){
                    callBack.onBooksLoaded(books);
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
}
