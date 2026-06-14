package main.java.service.factories;

import main.java.exceptions.LibrarySystemException;
import main.java.models.Publication;
import main.java.service.LibraryService;

public interface PublicationFactory {
    Publication create(int id, String title, String author, int year, String extraInfo)
            throws LibrarySystemException;
}