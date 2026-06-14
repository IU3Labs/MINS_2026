package ui.handlers;

import entity.Seat;
import entity.Ticket;
import entity.Screening;
import service.Result;
import service.ScreeningService;
import service.TicketService;
import ui.InputHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.TicketRenderer;

import java.util.List;
import java.util.UUID;

public class TicketsHandler {
    private final TicketService ticketService;
    private final ScreeningService screeningService;
    private final ScreeningsHandler screeningsHandler;
    private final InputHandler input;
    private final GeneralRenderer generalRenderer;
    private final TicketRenderer ticketRenderer;

    public TicketsHandler(TicketService ticketService, ScreeningService screeningService,
                          ScreeningsHandler screeningsHandler,
                          InputHandler input, GeneralRenderer generalRenderer, TicketRenderer ticketRenderer) {
        this.ticketService = ticketService;
        this.screeningService = screeningService;
        this.screeningsHandler = screeningsHandler;
        this.input = input;
        this.generalRenderer = generalRenderer;
        this.ticketRenderer = ticketRenderer;
    }

    public void handleShowAll() {
        List<Ticket> tickets = ticketService.getAll();
        ticketRenderer.printTickets(tickets);
    }

    public void handleBuy() {
        screeningsHandler.handleShowAll();
        UUID screeningId = input.readUuid("ID сеанса: ");

        var screeningResult = screeningService.getById(screeningId);
        if (screeningResult.isEmpty()) {
            generalRenderer.printError("Сеанс не найден");
            return;
        }

        Screening screening = screeningResult.get();
        List<Seat> takenSeats = ticketService.getTakenSeats(screeningId);
        ticketRenderer.printSeatMap(screening.getHall(), takenSeats);

        int row = input.readInt("Ряд: ");
        int number = input.readInt("Номер места: ");
        Seat seat = new Seat(row, number);

        var result = ticketService.buyTicket(screeningId, seat);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Билет куплен! ID: " + result.getValue());
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleUse() {
        screeningsHandler.handleShowAll();
        UUID screeningId = input.readUuid("ID сеанса: ");
        
        List<Ticket> tickets = ticketService.getPaidTicketsByScreening(screeningId);
        ticketRenderer.printTickets(tickets);
        
        if (tickets.isEmpty()) {
            generalRenderer.printError("Нет оплаченных билетов для этого сеанса");
            return;
        }
        
        UUID ticketId = input.readUuid("ID билета для использования: ");
        var result = ticketService.useTicket(ticketId);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Билет использован.");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleCancel() {
        screeningsHandler.handleShowAll();
        UUID screeningId = input.readUuid("ID сеанса: ");

        List<Ticket> tickets = ticketService.getPaidTicketsByScreening(screeningId);
        ticketRenderer.printTickets(tickets);
        
        UUID ticketId = input.readUuid("ID билета для отмены: ");
        var result = ticketService.cancelTicket(ticketId);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Билет отменён.");
        } else {
            generalRenderer.printError(result.getError());
        }
    }
}
