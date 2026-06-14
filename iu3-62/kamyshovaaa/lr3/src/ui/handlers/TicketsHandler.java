package ui.handlers;

import entity.Seat;
import entity.Ticket;
import entity.Screening;
import service.ScreeningService;
import service.TicketQueryService;
import service.TicketCommandService;
import ui.InputHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.TicketRenderer;

import java.util.List;
import java.util.UUID;

public class TicketsHandler {
    private final TicketQueryService ticketQueryService;
    private final TicketCommandService ticketCommandService;
    private final ScreeningService screeningService;
    private final ScreeningsHandler screeningsHandler;
    private final InputHandler input;
    private final GeneralRenderer generalRenderer;
    private final TicketRenderer ticketRenderer;

    public TicketsHandler(TicketQueryService ticketQueryService, TicketCommandService ticketCommandService,
                          ScreeningService screeningService,
                          ScreeningsHandler screeningsHandler,
                          InputHandler input, GeneralRenderer generalRenderer, TicketRenderer ticketRenderer) {
        this.ticketQueryService = ticketQueryService;
        this.ticketCommandService = ticketCommandService;
        this.screeningService = screeningService;
        this.screeningsHandler = screeningsHandler;
        this.input = input;
        this.generalRenderer = generalRenderer;
        this.ticketRenderer = ticketRenderer;
    }

    public void handleShowAll() {
        List<Ticket> tickets = ticketQueryService.getAll();
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
        
        showSeatMap(screeningId, screening);
    }

    private void showSeatMap(UUID screeningId, Screening screening) {
        List<Seat> takenSeats = ticketQueryService.getTakenSeats(screeningId);
        ticketRenderer.printSeatMap(screening.getHall(), takenSeats);

        int row = input.readInt("Ряд: ");
        int number = input.readInt("Номер места: ");
        Seat seat = new Seat(row, number);

        Ticket tempTicket = new Ticket(UUID.randomUUID(), screeningId, seat, screening.getTicketPrice());
        
        var pricingService = ticketQueryService.getPricingService();
        String menu = pricingService.getMenu();
        
        System.out.println("\nВыберите категорию:");
        System.out.print(menu);
        int category = input.readInt("Категория: ");
        
        pricingService.setStrategyByIndex(category - 1);
        double price = pricingService.calculatePrice(screening.getTicketPrice());
        String categoryName = pricingService.getCategoryName();
        
        System.out.printf("Цена: %.2f руб. (%s)%n", price, categoryName);
        
        String confirm = input.readLine("Купить? (да/нет): ");
        if (!"да".equalsIgnoreCase(confirm)) {
            generalRenderer.printError("Покупка отменена.");
            return;
        }

        var result = ticketCommandService.buyTicket(screeningId, seat, category - 1);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Билет куплен! ID: " + result.getValue());
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleUse() {
        screeningsHandler.handleShowAll();
        UUID screeningId = input.readUuid("ID сеанса: ");
        
        List<Ticket> tickets = ticketQueryService.getPaidTicketsByScreening(screeningId);
        ticketRenderer.printTickets(tickets);
        
        if (tickets.isEmpty()) {
            generalRenderer.printError("Нет оплаченных билетов для этого сеанса");
            return;
        }
        
        UUID ticketId = input.readUuid("ID билета для использования: ");
        var result = ticketCommandService.useTicket(ticketId);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Билет использован.");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleCancel() {
        screeningsHandler.handleShowAll();
        UUID screeningId = input.readUuid("ID сеанса: ");

        List<Ticket> tickets = ticketQueryService.getPaidTicketsByScreening(screeningId);
        ticketRenderer.printTickets(tickets);
        
        UUID ticketId = input.readUuid("ID билета для отмены: ");
        var result = ticketCommandService.cancelTicket(ticketId);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Билет отменён.");
        } else {
            generalRenderer.printError(result.getError());
        }
    }
}
