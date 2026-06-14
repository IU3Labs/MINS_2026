package ui.handlers;

import entity.Ticket;
import entity.Screening;
import entity.state.TicketStatus;
import repository.TicketRepository;
import repository.ScreeningRepository;
import repository.impls.TicketRepositoryImpl;
import repository.impls.ScreeningRepositoryImpl;
import service.CrudService;
import service.impls.CrudServiceImpl;
import ui.InputHandler;
import ui.renderer.GeneralRenderer;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RevenuesHandler {

    private TicketRepository ticketRepository;
    private ScreeningRepository screeningRepository;

    private CrudService<Ticket> ticketCrud;
    private CrudService<Screening> screeningCrud;

    private final InputHandler input;
    private final GeneralRenderer generalRenderer;

    public RevenuesHandler(InputHandler input, GeneralRenderer generalRenderer) {
        this.input = input;
        this.generalRenderer = generalRenderer;
        
        this.ticketRepository = new TicketRepositoryImpl();
        this.screeningRepository = new ScreeningRepositoryImpl();

        this.ticketCrud = new CrudServiceImpl<>(ticketRepository);
        this.screeningCrud = new CrudServiceImpl<>(screeningRepository);
    }
    
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketCrud = new CrudServiceImpl<>(ticketRepository);
    }

    public void setScreeningRepository(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
        this.screeningCrud = new CrudServiceImpl<>(screeningRepository);
    }

    public void handleShowMenu() {
        while (true) {
            System.out.println("""
                    
                    === Выручка ===
                    1. Общая выручка
                    2. Выручка за сеанс
                    3. Выручка за дату
                    4. Выручка за период
                    5. Всего продано билетов
                    6. Продано билетов за сеанс
                    0. Назад
                    """);

            int choice = input.readInt("Выберите пункт: ");

            switch (choice) {
                case 1 -> showTotalRevenue();
                case 2 -> showRevenueByScreening();
                case 3 -> showRevenueByDate();
                case 4 -> showRevenueByDateRange();
                case 5 -> showTotalTicketsSold();
                case 6 -> showTicketsSoldByScreening();
                case 0 -> { return; }
                default -> generalRenderer.printError("Неверный пункт");
            }
        }
    }

    private void showTotalRevenue() {
        double revenue = getTotalRevenue();
        int tickets = getTotalTicketsSold();
        System.out.printf("Общая выручка: %.2f руб.%n", revenue);
        System.out.printf("Всего продано билетов: %d%n", tickets);
    }

    private void showRevenueByScreening() {
        var screenings = getAllScreenings();
        if (screenings.isEmpty()) {
            generalRenderer.printError("Нет сеансов");
            return;
        }
        
        System.out.printf("%-38s | %-17s | %-14s | %-10s | %-11s | %s%n", 
            "ID", "Фильм", "Зал", "Время", "Цена", "Дата");
        generalRenderer.printSeparator(110);
        for (var s : screenings) {
            System.out.printf("%-38s | %-17s | %-14s | %-10s | %-11s | %s%n",
                s.getId(),
                s.getMovieTitle(),
                s.getHallName(),
                s.getStartTime().toLocalTime(),
                s.getTicketPrice(),
                s.getStartTime().toLocalDate());
        }
        
        UUID screeningId = input.readUuid("ID сеанса: ");
        double revenue = getRevenueByScreening(screeningId);
        int tickets = getTicketsSoldByScreening(screeningId);
        System.out.printf("Выручка за сеанс: %.2f руб.%n", revenue);
        System.out.printf("Продано билетов: %d%n", tickets);
    }

    private void showRevenueByDate() {
        LocalDate date = input.readDate("Дата (yyyy-MM-dd): ");
        double revenue = getRevenueByDate(date);
        var tickets = getPaidTicketsByDate(date);
        System.out.printf("Выручка за %s: %.2f руб.%n", date, revenue);
        System.out.printf("Продано билетов: %d%n", tickets.size());
    }

    private void showRevenueByDateRange() {
        LocalDate startDate = input.readDate("Начальная дата (yyyy-MM-dd): ");
        LocalDate endDate = input.readDate("Конечная дата (yyyy-MM-dd): ");
        double revenue = getRevenueByDateRange(startDate, endDate);
        var tickets = getPaidTicketsByDateRange(startDate, endDate);
        System.out.printf("Выручка за период %s - %s: %.2f руб.%n", startDate, endDate, revenue);
        System.out.printf("Продано билетов: %d%n", tickets.size());
    }

    private void showTotalTicketsSold() {
        int tickets = getTotalTicketsSold();
        System.out.printf("Всего продано билетов: %d%n", tickets);
    }

    private void showTicketsSoldByScreening() {
        var screenings = getAllScreenings();
        if (screenings.isEmpty()) {
            generalRenderer.printError("Нет сеансов");
            return;
        }
        
        System.out.printf("%-38s | %-17s | %-14s | %-10s | %-11s | %s%n", 
            "ID", "Фильм", "Зал", "Время", "Цена", "Дата");
        generalRenderer.printSeparator(110);
        for (var s : screenings) {
            System.out.printf("%-38s | %-17s | %-14s | %-10s | %-11s | %s%n",
                s.getId(),
                s.getMovieTitle(),
                s.getHallName(),
                s.getStartTime().toLocalTime(),
                s.getTicketPrice(),
                s.getStartTime().toLocalDate());
        }
        
        UUID screeningId = input.readUuid("ID сеанса: ");
        int tickets = getTicketsSoldByScreening(screeningId);
        System.out.printf("Продано билетов за сеанс: %d%n", tickets);
    }

    // То, что должно быть в сервисе
    
    public double getTotalRevenue() {
        return getPaidTickets().stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public double getRevenueByScreening(UUID screeningId) {
        return getPaidTicketsByScreening(screeningId).stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public double getRevenueByDate(LocalDate date) {
        return getPaidTicketsByDate(date).stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public double getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        return getPaidTicketsByDateRange(startDate, endDate).stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public List<Ticket> getPaidTickets() {
        return ticketCrud.getAll().stream()
                .filter(t -> t.getStatus() == TicketStatus.PAID)
                .collect(Collectors.toList());
    }

    public List<Ticket> getPaidTicketsByScreening(UUID screeningId) {
        return ticketCrud.getAll().stream()
                .filter(t -> t.getScreeningId().equals(screeningId))
                .filter(t -> t.getStatus() == TicketStatus.PAID)
                .collect(Collectors.toList());
    }

    public List<Ticket> getPaidTicketsByDate(LocalDate date) {
        return getPaidTickets().stream()
                .filter(t -> {
                    Screening screening = screeningCrud.getById(t.getScreeningId()).orElse(null);
                    return screening != null && 
                           screening.getStartTime().toLocalDate().equals(date);
                })
                .collect(Collectors.toList());
    }

    public List<Ticket> getPaidTicketsByDateRange(LocalDate startDate, LocalDate endDate) {
        return getPaidTickets().stream()
                .filter(t -> {
                    Screening screening = screeningCrud.getById(t.getScreeningId()).orElse(null);
                    if (screening == null) return false;
                    LocalDate ticketDate = screening.getStartTime().toLocalDate();
                    return !ticketDate.isBefore(startDate) && !ticketDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }

    public int getTotalTicketsSold() {
        return getPaidTickets().size();
    }

    public int getTicketsSoldByScreening(UUID screeningId) {
        return getPaidTicketsByScreening(screeningId).size();
    }

    public List<Screening> getAllScreenings() {
        return screeningCrud.getAll();
    }
}