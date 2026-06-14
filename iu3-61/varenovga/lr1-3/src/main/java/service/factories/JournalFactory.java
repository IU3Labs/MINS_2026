package main.java.service.factories;

import main.java.exceptions.LibrarySystemException;
import main.java.interfaces.PublicationFactory;
import main.java.models.Journal;
import main.java.models.Publication;

public class JournalFactory implements PublicationFactory {
    @Override
    public Publication create(int id, String title, String author, int year, String extraInfo)
            throws LibrarySystemException {
        int issueNumber;
        try {
            issueNumber = Integer.parseInt(extraInfo.trim());
            if (issueNumber <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new LibrarySystemException("Номер выпуска должен быть положительным числом");
        }
        return new Journal(id, title, author, year, issueNumber);
    }
}