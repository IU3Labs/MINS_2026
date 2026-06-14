package interfaces;

import exceptions.LibrarySystemException;
import exceptions.UserNotFoundException;
import service.dto.OverdueInfo;
import service.dto.ActiveLoanInfo;
import service.dto.UserLoanHistory;
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