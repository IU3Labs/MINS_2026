package service.dto;
import java.time.LocalDate;
public record ActiveLoanInfo(
        String publicationTitle,
        String userName,
        LocalDate borrowDate,
        LocalDate dueDate,
        boolean isOverdue
) {}