package main.java.exceptions;

public class PublicationNotFoundException extends LibrarySystemException {
    public PublicationNotFoundException(int id) {
        super("Издание с ID " + id + " не найдено.");
    }
}