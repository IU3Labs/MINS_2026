package main.java.interfaces;

import main.java.exceptions.LibrarySystemException;
import main.java.exceptions.UserNotFoundException;
import main.java.service.dto.OverdueInfo;
import main.java.service.dto.ActiveLoanInfo;
import main.java.service.dto.UserLoanHistory;
import java.time.LocalDate;
import java.util.List;

public interface LoanServiceInterface {
    void borrowPublication(int pubId, int userId, LocalDate dueDate) throws LibrarySystemException;
    void borrowPublicationForTest(int pubId, int userId, LocalDate dueDate) throws LibrarySystemException;
    void returnPublication(int pubId) throws LibrarySystemException;
    boolean hasActiveLoans(int userId);
    List<OverdueInfo> getOverdueReport();
    List<ActiveLoanInfo> getActiveLoans();
    List<UserLoanHistory> getUserHistory(int userId) throws UserNotFoundException;
}