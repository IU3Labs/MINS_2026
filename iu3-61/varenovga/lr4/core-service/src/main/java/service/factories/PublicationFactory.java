package service.factories;

import exceptions.LibrarySystemException;
import models.Publication;
import service.LibraryService;

public interface PublicationFactory {
    Publication create(int id, String title, String author, int year, String extraInfo)
            throws LibrarySystemException;
}