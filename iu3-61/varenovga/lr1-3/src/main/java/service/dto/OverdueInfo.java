package main.java.service.dto;
import java.time.LocalDate;
public record OverdueInfo(
        String publicationTitle,
        String userName,
        String userEmail,
        LocalDate dueDate,
        long daysOverdue
) {}