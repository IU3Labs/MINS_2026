package interfaces;

import exceptions.LibrarySystemException;
import models.Publication;
import service.LibraryService;
import java.util.List;

public interface PublicationServiceInterface {
    Publication createPublication(
            LibraryService.PublicationType type,
            String title,
            String author,
            int year,
            String extraInfo
    ) throws LibrarySystemException;

    List<Publication> searchByTitle(String query);
    List<Publication> searchByAuthor(String query);
    List<Publication> getCatalog();
    Publication findById(int id) throws LibrarySystemException;
    void deletePublication(int pubId) throws LibrarySystemException;
    int getCount();
}