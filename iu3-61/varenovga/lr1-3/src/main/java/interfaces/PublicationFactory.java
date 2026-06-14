package main.java.interfaces;

import main.java.exceptions.LibrarySystemException;
import main.java.models.Publication;

public interface PublicationFactory {
    Publication create(int id, String title, String author, int year, String extraInfo) throws LibrarySystemException;
}