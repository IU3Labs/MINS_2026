package service.fines;

import java.time.LocalDate;
public interface FineCalculationStrategy {
    double calculateFine(LocalDate dueDate, LocalDate returnDate);
    String getDescription();
}