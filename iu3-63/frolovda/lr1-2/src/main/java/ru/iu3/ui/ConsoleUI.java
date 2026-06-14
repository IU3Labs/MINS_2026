package ru.iu3.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.handlers.BookingsMenuHandler;
import ru.iu3.ui.handlers.PassesMenuHandler;
import ru.iu3.ui.handlers.RoomsMenuHandler;
import ru.iu3.ui.items.BookingsMenuItem;
import ru.iu3.ui.items.ExitMenuItem;
import ru.iu3.ui.items.GenerateDataMenuItem;
import ru.iu3.ui.items.PassesMenuItem;
import ru.iu3.ui.items.RoomsMenuItem;
import ru.iu3.ui.interfaces.MenuItem;

public class ConsoleUI {
    private MainMenu mainMenuRunner;

    public ConsoleUI(Scanner scanner, RoomService roomService, PassService passService, BookingService bookingService) {
        OutputUI display = new OutputUI();
        RoomsMenuHandler roomsMenuHandler = new RoomsMenuHandler(scanner, roomService);
        PassesMenuHandler passesMenuHandler = new PassesMenuHandler(scanner, passService, bookingService);
        BookingsMenuHandler bookingsMenuHandler = new BookingsMenuHandler(scanner, bookingService,
                roomsMenuHandler, passesMenuHandler);

        List<MenuItem> mainItems = Arrays.asList(
                new RoomsMenuItem(display, roomsMenuHandler),
                new BookingsMenuItem(display, bookingsMenuHandler),
                new PassesMenuItem(display, passesMenuHandler),
                new GenerateDataMenuItem(roomService, passService, bookingService),
                new ExitMenuItem(display)
        );

        this.mainMenuRunner = new MainMenu(scanner, display, mainItems);
    }

    public void start() {
        mainMenuRunner.run();
    }
}
