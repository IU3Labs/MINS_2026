package ui.renderer;

import entity.Hall;
import entity.Seat;
import entity.Ticket;
import entity.state.TicketStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketRenderer {

    private final GeneralRenderer generalRenderer;

    public TicketRenderer(GeneralRenderer generalRenderer) {
        this.generalRenderer = generalRenderer;
    }

    public void printTickets(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            generalRenderer.printNotFound("Билеты");
            return;
        }
        System.out.printf("%-38s | %-10s | %-12s%n", "ID", "Статус", "Место");
        generalRenderer.printSeparator(70);
        for (Ticket t : tickets) {
            Seat seat = t.getSeat();
            System.out.printf("%-38s | %-10s | %d-%d%n",
                    t.getId(),
                    translateStatus(t.getStatus()),
                    seat.getRow(),
                    seat.getNumber());
        }
    }

    public void printSeatMap(Hall hall, List<Seat> takenSeats) {
        Set<Seat> taken = new HashSet<>(takenSeats);
        
        System.out.println("\n   ЭКРАН");
        int width = hall.getSeatsPerRow() * 2 + 2;
        System.out.println("   " + "-".repeat(width));
        System.out.print("     ");
        for (int n = 1; n <= hall.getSeatsPerRow(); n++) {
            System.out.printf("%2s", n);
        }
        System.out.println();
        System.out.println("   " + "-".repeat(width));

        for (int r = 1; r <= hall.getRows(); r++) {
            System.out.printf("%2d |", r);
            for (int n = 1; n <= hall.getSeatsPerRow(); n++) {
                Seat seat = new Seat(r, n);
                System.out.print(taken.contains(seat) ? " X" : " .");
            }
            System.out.println();
        }
        System.out.println("\n[. = свободно, X = занято]");
    }

    private String translateStatus(TicketStatus status) {
        return switch (status) {
            case AVAILABLE -> "Доступен";
            case PAID -> "Оплачен";
            case USED -> "Использован";
        };
    }
}
