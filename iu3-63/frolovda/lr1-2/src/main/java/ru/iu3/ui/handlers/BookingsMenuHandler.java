package ru.iu3.ui.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.Booking;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;

public class BookingsMenuHandler {

    private Scanner scanner;
    private BookingService bookingService;
    private RoomsMenuHandler roomsMenuHandler;
    private PassesMenuHandler passesMenuHandler;
    private List<MenuItem> items = new ArrayList<>();

    public BookingsMenuHandler(Scanner scanner, BookingService bookingService,
            RoomsMenuHandler roomsMenuHandler, PassesMenuHandler passesMenuHandler) {
        this.scanner = scanner;
        this.bookingService = bookingService;
        this.roomsMenuHandler = roomsMenuHandler;
        this.passesMenuHandler = passesMenuHandler;
        items.add(new ShowBookingsItem());
        items.add(new AddBookingItem());
        items.add(new CancelBookingItem());
        items.add(new BackItem());
    }

    public void run() {
        boolean inBookingsMenu = true;
        while (inBookingsMenu) {
            for (MenuItem item : items) {
                System.out.println(item.getKey() + ". " + item.getLabel());
            }
            try {
                System.out.print(UiConstants.PROMPT_MENU);
                int choice = Integer.parseInt(scanner.nextLine());
                MenuItem selected = findItem(choice);
                if (selected != null) {
                    inBookingsMenu = selected.execute();
                } else {
                    System.out.println(UiConstants.INVALID_CHOICE);
                }
            } catch (Exception e) {
                System.out.println(UiConstants.ERROR_PREFIX + e.getMessage());
            }
        }
    }

    public void showRooms() {
        roomsMenuHandler.showRooms();
    }

    public void showPasses() {
        passesMenuHandler.showPasses();
    }

    private void showBookings() {
        for (Booking booking : bookingService.getAllBookings()) {
            System.out.println(String.format(UiConstants.BOOKING_LINE_FORMAT, booking.getId(), booking.getRoomId(),
                    booking.getStartTime(), booking.getEndTime(), booking.getPassId()));
        }
    }

    private MenuItem findItem(int key) {
        for (MenuItem item : items) {
            if (item.getKey() == key) {
                return item;
            }
        }
        return null;
    }

    private class ShowBookingsItem implements MenuItem {
        @Override
        public int getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return UiConstants.BOOKING_MENU_SHOW;
        }

        @Override
        public boolean execute() {
            showBookings();
            return true;
        }
    }

    private class AddBookingItem implements MenuItem {
        @Override
        public int getKey() {
            return 2;
        }

        @Override
        public String getLabel() {
            return UiConstants.BOOKING_MENU_ADD;
        }

        @Override
        public boolean execute() {
            roomsMenuHandler.showRooms();
            passesMenuHandler.showPasses();

            System.out.println(UiConstants.PROMPT_BOOKING_ROOM_ID);
            int roomId = Integer.parseInt(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_BOOKING_MONTH);
            int month = Integer.parseInt(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_BOOKING_DAY);
            int day = Integer.parseInt(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_BOOKING_START_TIME);
            LocalTime startTime = LocalTime.parse(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_BOOKING_END_TIME);
            LocalTime endTime = LocalTime.parse(scanner.nextLine());

            int year = LocalDate.now().getYear();
            LocalDate date = LocalDate.of(year, month, day);
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
            System.out.println(UiConstants.PROMPT_BOOKING_PASS_ID);
            int passId = Integer.parseInt(scanner.nextLine());
            double cost = bookingService.createBooking(roomId, passId, startDateTime, endDateTime);
            System.out.println(UiConstants.BOOKING_CREATED + cost + UiConstants.BOOKING_CREATED_SUFFIX);

            return true;
        }
    }

    private class CancelBookingItem implements MenuItem {
        @Override
        public int getKey() {
            return 3;
        }

        @Override
        public String getLabel() {
            return UiConstants.BOOKING_MENU_CANCEL;
        }

        @Override
        public boolean execute() {
            showBookings();

            System.out.println(UiConstants.PROMPT_BOOKING_CANCEL);
            int id = Integer.parseInt(scanner.nextLine());
            bookingService.cancelBooking(id);
            return true;
        }
    }

    private class BackItem implements MenuItem {
        @Override
        public int getKey() {
            return 0;
        }

        @Override
        public String getLabel() {
            return UiConstants.BOOKING_MENU_BACK;
        }

        @Override
        public boolean execute() {
            return false;
        }
    }
}
