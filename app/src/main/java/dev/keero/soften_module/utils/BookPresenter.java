package dev.keero.soften_module.utils;

import java.util.ArrayList;

import dev.keero.soften_module.model.Book;
public class BookPresenter {
    // Dummy data.
    private final static String[] bookName = {
            "Book 1",
            "Book 2",
            "Book 3",
            "Book 4",
            "Book 5",
            "Book 6"
    };

    private final static String[] bookAuthor = {
            "Author 1",
            "Author 2",
            "Author 3",
            "Author 4",
            "Author 5",
            "Author 6"
    };


    private final static String type = "Sex";
    public ArrayList<Book> loadBook(final ArrayList<Book> books){

        for(int i = 0; i < bookName.length; i++){
            Book bookItem = new Book(i, bookName[i], bookAuthor[i], type);
            books.add(bookItem);
        }

        return books;
    }
}
