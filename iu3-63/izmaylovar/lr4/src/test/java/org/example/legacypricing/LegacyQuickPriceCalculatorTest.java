package org.example.legacypricing;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegacyQuickPriceCalculatorTest {
    @Test
    void calculatesQuickPriceWithoutSavingAnything() {
        Session session = new Session(
                UUID.randomUUID(),
                new Movie(UUID.randomUUID(), "Dune", 166, "Sci-Fi", "16+"),
                new Hall(UUID.randomUUID(), "Blue Hall", 6, 8),
                LocalDateTime.of(2026, 4, 11, 11, 0),
                new BigDecimal("500")
        );

        LegacyQuickPriceQuote quote = new LegacyQuickPriceCalculator().calculate(session, 1, 4);

        assertEquals(new BigDecimal("2196.00"), quote.finalTotal());
        assertTrue(quote.appliedRules().size() >= 4);
    }
}
