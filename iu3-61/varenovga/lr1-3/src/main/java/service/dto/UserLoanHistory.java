package main.java.service.dto;

import java.time.LocalDate;
public record UserLoanHistory(
        String publicationTitle,
        LocalDate borrowDate,
        LocalDate dueDate,
        boolean isOverdue
) {}