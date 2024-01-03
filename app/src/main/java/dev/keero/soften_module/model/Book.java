package dev.keero.soften_module.model;

public class Book {
    private final String id;
    private final String bookName;
    private final String bookAuthor;
    private final String bookType;

    public Book(String id, String bookName, String bookAuthor, String bookType) {
        this.id = id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookType = bookType;
    }

    public String getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookType() {
        return bookType;
    }
}
