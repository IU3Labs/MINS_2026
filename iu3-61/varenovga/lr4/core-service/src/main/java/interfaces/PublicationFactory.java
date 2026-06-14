package interfaces;

import exceptions.LibrarySystemException;
import models.Publication;

public interface PublicationFactory {
    Publication create(int id, String title, String author, int year, String extraInfo) throws LibrarySystemException;
}