package ui.renderer;

import entity.Hall;

import java.util.List;

public class HallRenderer {

    private final GeneralRenderer generalRenderer;

    public HallRenderer(GeneralRenderer generalRenderer) {
        this.generalRenderer = generalRenderer;
    }

    public void printHalls(List<Hall> halls) {
        if (halls.isEmpty()) {
            generalRenderer.printNotFound("Залы");
            return;
        }
        System.out.printf("%-38s | %-15s | %-10s | %s%n", "ID", "Название", "Ряды", "Мест в ряду");
        generalRenderer.printSeparator(80);
        for (Hall h : halls) {
            System.out.printf("%-38s | %-15s | %-10d | %d%n",
                    h.getId(), h.getName(), h.getRows(), h.getSeatsPerRow());
        }
    }
}
