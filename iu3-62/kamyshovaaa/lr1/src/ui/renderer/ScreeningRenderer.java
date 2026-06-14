package ui.renderer;

import entity.Screening;

import java.util.List;

public class ScreeningRenderer {

    private final GeneralRenderer generalRenderer;

    public ScreeningRenderer(GeneralRenderer generalRenderer) {
        this.generalRenderer = generalRenderer;
    }

    public void printScreenings(List<Screening> screenings) {
        if (screenings.isEmpty()) {
            generalRenderer.printNotFound("Сеансы");
            return;
        }
        System.out.printf("%-38s | %-20s | %-15s | %-10s | %-12s | %s%n", "ID", "Фильм", "Зал", "Время", "Цена", "Дата");
        generalRenderer.printSeparator(120);
        for (Screening s : screenings) {
            System.out.printf("%-38s | %-20s | %-15s | %-10s | %-12.2f | %s%n",
                    s.getId(),
                    s.getMovie().getTitle(),
                    s.getHall().getName(),
                    s.getStartTime().toLocalTime(),
                    s.getTicketPrice(),
                    s.getStartTime().toLocalDate());
        }
    }
}
