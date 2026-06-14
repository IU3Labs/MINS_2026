package models;

import interfaces.DateProvider;
import java.time.LocalDate;

public class LoanRecord {
    private Publication publication;
    private int userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean isReturned;

    public LoanRecord(Publication publication, int userId, LocalDate dueDate, DateProvider dateProvider) {
        this.publication = publication;
        this.userId = userId;
        this.borrowDate = dateProvider.now();
        this.dueDate = dueDate;
        this.isReturned = false;
    }

    public boolean isOverdue(LocalDate today) {
        return !isReturned && today.isAfter(dueDate);
    }

    public Publication getPublication() { return publication; }
    public int getUserId() { return userId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isReturned() { return isReturned; }
    public void markReturned() { isReturned = true; }

    public long getDaysOverdue(LocalDate today) {
        if (!isOverdue(today)) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
    }
}