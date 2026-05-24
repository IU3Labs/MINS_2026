package org.example.legacypricing;

import org.example.domain.exception.ValidationException;
import org.example.domain.model.Session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// Intentionally implemented as a legacy module for anti-pattern demonstration.
public class LegacyQuickPriceCalculator {
    public LegacyQuickPriceQuote calculate(Session session, int row, int ticketsCount) {
        if (ticketsCount <= 0) {
            throw new ValidationException("Tickets count should be greater than 0");
        }
        if (row <= 0 || row > session.getHall().getRows()) {
            throw new ValidationException("Row should exist in the selected hall");
        }

        BigDecimal baseTotal = session.getPrice().multiply(BigDecimal.valueOf(ticketsCount));
        BigDecimal total = baseTotal;
        List<String> appliedRules = new ArrayList<>();

        if (row <= 2) {
            total = total.multiply(new BigDecimal("1.20"));
            appliedRules.add("Rows 1-2 premium surcharge applied");
        }

        if (row >= session.getHall().getRows() - 1) {
            total = total.multiply(new BigDecimal("0.90"));
            appliedRules.add("Back rows comfort discount applied");
        }

        if (ticketsCount >= 4) {
            total = total.multiply(new BigDecimal("0.85"));
            appliedRules.add("Group discount for 4+ tickets applied");
        }

        if (session.getStartTime().getHour() < 12) {
            total = total.multiply(new BigDecimal("0.80"));
            appliedRules.add("Morning session discount applied");
        }

        if (session.getStartTime().getDayOfWeek().getValue() >= 5) {
            total = total.multiply(new BigDecimal("1.25"));
            appliedRules.add("Weekend surcharge applied");
        }

        total = total.add(BigDecimal.valueOf(39L * ticketsCount));
        appliedRules.add("Service fee added");

        if (total.compareTo(new BigDecimal("5000")) > 0) {
            total = total.subtract(new BigDecimal("300"));
            appliedRules.add("Large order manual correction applied");
        }

        return new LegacyQuickPriceQuote(
                session.getId(),
                session.getMovie().getTitle(),
                row,
                ticketsCount,
                baseTotal.setScale(2, RoundingMode.HALF_UP),
                total.setScale(2, RoundingMode.HALF_UP),
                List.copyOf(appliedRules)
        );
    }
}
