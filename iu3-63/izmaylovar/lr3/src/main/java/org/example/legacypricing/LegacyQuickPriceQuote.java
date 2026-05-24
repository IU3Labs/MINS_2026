package org.example.legacypricing;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record LegacyQuickPriceQuote(
        UUID sessionId,
        String movieTitle,
        int row,
        int ticketsCount,
        BigDecimal baseTotal,
        BigDecimal finalTotal,
        List<String> appliedRules
) {
    public String toDisplayString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n=== Legacy Quick Price Calculator ===\n")
                .append("Session ID: ").append(sessionId).append('\n')
                .append("Movie: ").append(movieTitle).append('\n')
                .append("Row: ").append(row).append('\n')
                .append("Tickets: ").append(ticketsCount).append('\n')
                .append("Base total: ").append(baseTotal).append('\n')
                .append("Final total: ").append(finalTotal).append('\n')
                .append("Applied rules:\n");

        if (appliedRules.isEmpty()) {
            builder.append("- no adjustments");
        } else {
            for (String rule : appliedRules) {
                builder.append("- ").append(rule).append('\n');
            }
        }

        return builder.toString().stripTrailing();
    }
}
