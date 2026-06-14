package ru.iu3.ui.handlers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.Booking;
import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.MenuRunner;
import ru.iu3.ui.MenuRunnerHelper;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;

public class ReportsMenuHandler {

    private RoomService roomService;
    private PassService passService;
    private BookingService bookingService;
    private List<MenuItem> items = new ArrayList<>();
    private MenuRunner menuRunner;

    public ReportsMenuHandler(Scanner scanner, RoomService roomService, PassService passService,
            BookingService bookingService) {
        this.roomService = roomService;
        this.passService = passService;
        this.bookingService = bookingService;
        items.add(new ActiveBookingsReportItem());
        items.add(new RoomsLoadReportItem());
        items.add(new PassesReportItem());
        items.add(new BackItem());
        this.menuRunner = new MenuRunner(scanner, new MenuRunnerHelper(), items);
    }

    public void run() {
        menuRunner.run();
    }

    private void showBookingsReport() {
        System.out.println(UiConstants.REPORT_ACTIVE_BOOKINGS_TITLE);
        boolean hasRows = false;
        for (Booking booking : bookingService.getAllBookings()) {
            if (!booking.isActive()) {
                continue;
            }
            Room room = roomService.getRoomById(booking.getRoomId());
            Pass pass = passService.getPassById(booking.getPassId());
            System.out.println(String.format(UiConstants.REPORT_ACTIVE_BOOKING_ROW, booking.getId(), room.getId(),
                    room.getName(), pass.getId(), pass.getHolderName(), booking.getStartTime(),
                    booking.getEndTime()));
            hasRows = true;
        }
        if (!hasRows) {
            System.out.println(UiConstants.REPORT_NO_ACTIVE_BOOKINGS);
        }
    }

    private void showLoadReport() {
        System.out.println(UiConstants.REPORT_ROOMS_LOAD_TITLE);
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println(UiConstants.ROOMS_EMPTY);
            return;
        }

        for (Room room : rooms) {
            int bookingsCount = 0;
            long totalMinutes = 0;
            double totalCost = 0;

            for (Booking booking : bookingService.getAllBookings()) {
                if (booking.isActive() && booking.getRoomId() == room.getId()) {
                    bookingsCount++;
                    long minutes = Duration.between(booking.getStartTime(), booking.getEndTime()).toMinutes();
                    totalMinutes += minutes;
                    totalCost += calculateGreedyCost(room, minutes);
                }
            }

            System.out.println(String.format(UiConstants.REPORT_ROOM_LOAD_ROW, room.getId(), room.getName(),
                    bookingsCount, totalMinutes, totalMinutes / 60.0, totalCost));
        }
    }

    private void showPassesReport() {
        System.out.println(UiConstants.REPORT_PASSES_TITLE);
        List<Pass> passes = passService.getAllPasses();
        if (passes.isEmpty()) {
            System.out.println(UiConstants.REPORT_NO_PASSES);
            return;
        }

        for (Pass pass : passes) {
            int activeBookingsCount = 0;
            List<String> roomNames = new ArrayList<>();

            for (Booking booking : bookingService.getAllBookings()) {
                if (booking.isActive() && booking.getPassId() == pass.getId()) {
                    activeBookingsCount++;
                    Room room = roomService.getRoomById(booking.getRoomId());
                    roomNames.add(room.getId() + " " + room.getName());
                }
            }

            String names = roomNames.isEmpty() ? UiConstants.REPORT_NO_ROOMS_FOR_PASS : String.join(", ", roomNames);
            
            System.out.println(String.format(UiConstants.REPORT_PASS_ROW, pass.getId(), pass.getHolderName(),
                    pass.isActive(), activeBookingsCount, names));
        }
    }

    private double calculateGreedyCost(Room room, long minutes) {
        double hours = minutes / 60.0;
        long billedHours = (long) Math.ceil(hours);
        return billedHours * room.getHourlyRate();
    }

    private class ActiveBookingsReportItem implements MenuItem {
        @Override
        public int getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return UiConstants.REPORT_MENU_ACTIVE_BOOKINGS;
        }

        @Override
        public boolean execute() {
            showBookingsReport();
            return true;
        }
    }

    private class RoomsLoadReportItem implements MenuItem {
        @Override
        public int getKey() {
            return 2;
        }

        @Override
        public String getLabel() {
            return UiConstants.REPORT_MENU_ROOMS_LOAD;
        }

        @Override
        public boolean execute() {
            showLoadReport();
            return true;
        }
    }

    private class PassesReportItem implements MenuItem {
        @Override
        public int getKey() {
            return 3;
        }

        @Override
        public String getLabel() {
            return UiConstants.REPORT_MENU_PASSES;
        }

        @Override
        public boolean execute() {
            showPassesReport();
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
            return UiConstants.REPORT_MENU_BACK;
        }

        @Override
        public boolean execute() {
            return false;
        }
    }
}
