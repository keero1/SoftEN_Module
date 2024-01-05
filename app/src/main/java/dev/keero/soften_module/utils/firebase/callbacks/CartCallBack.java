package dev.keero.soften_module.utils.firebase.callbacks;

import java.util.ArrayList;

import dev.keero.soften_module.model.Book;

public interface CartCallBack {
    void onCartDetailsLoaded(ArrayList<Book> books);
}
