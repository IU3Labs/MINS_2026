package models;

public class Book extends Publication {
    private String isbn;

    public Book(int id, String title, String author, int year, String isbn) {
        super(id, title, author, year);
        this.isbn = isbn;
    }

    @Override
    public String getType() { return "Книга"; }
    public String getIsbn() { return isbn; }

    @Override
    public String toString() {
        return String.format("%s [Автор: %s, ISBN: %s]", super.toString(), author, isbn);
    }
}