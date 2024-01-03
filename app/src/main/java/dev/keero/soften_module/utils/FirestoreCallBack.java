package dev.keero.soften_module.utils;

import java.util.ArrayList;

import dev.keero.soften_module.model.Book;

public interface FirestoreCallBack {
    void onBooksLoaded(ArrayList<Book> book);
}
