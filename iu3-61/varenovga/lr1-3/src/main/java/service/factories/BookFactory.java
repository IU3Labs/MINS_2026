package main.java.service.factories;

import main.java.exceptions.LibrarySystemException;
import main.java.interfaces.PublicationFactory;
import main.java.models.Book;
import main.java.models.Publication;

public class BookFactory implements PublicationFactory {
    @Override
    public Publication create(int id, String title, String author, int year, String extraInfo)
            throws LibrarySystemException {
        if (extraInfo == null || extraInfo.trim().isEmpty()) {
            throw new LibrarySystemException("ISBN не может быть пустым для книги");
        }
        return new Book(id, title, author, year, extraInfo.trim());
    }
}