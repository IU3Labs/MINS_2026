package service.factories;

import exceptions.LibrarySystemException;
import interfaces.PublicationFactory;
import models.Book;
import models.Publication;

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