package service.fines;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class NotSimpleFineStrategy implements FineCalculationStrategy {
    private final double finePerDay;

    public NotSimpleFineStrategy() {
        this.finePerDay = 100.0;
    }

    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isBefore(dueDate) || returnDate.isEqual(dueDate)) {
            return 0.0;
        }
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysOverdue * finePerDay;
    }

    @Override
    public String getDescription() {
        return "НеПростой тариф: " + finePerDay + " руб/день";
    }
}