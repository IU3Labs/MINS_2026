package main.java.exceptions;

public class PublicationUnavailableException extends LibrarySystemException {
    public PublicationUnavailableException(int id) {
        super("Издание с ID " + id + " недоступно для выдачи.");
    }
}