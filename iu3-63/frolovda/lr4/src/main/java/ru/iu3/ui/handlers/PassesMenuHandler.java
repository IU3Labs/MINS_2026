package ru.iu3.ui.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.ui.MenuRunner;
import ru.iu3.ui.MenuRunnerHelper;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;

public class PassesMenuHandler {

    private Scanner scanner;
    private PassService passService;
    private BookingService bookingService;
    private List<MenuItem> items = new ArrayList<>();
    private MenuRunner menuRunner;

    public PassesMenuHandler(Scanner scanner, PassService passService, BookingService bookingService) {
        this.scanner = scanner;
        this.passService = passService;
        this.bookingService = bookingService;
        items.add(new ShowPassesItem());
        items.add(new IssuePassItem());
        items.add(new DeactivatePassItem());
        items.add(new ShowRoomsForPassItem());
        items.add(new BackItem());
        this.menuRunner = new MenuRunner(scanner, new MenuRunnerHelper(), items);
    }

    public void run() {
        menuRunner.run();
    }

    public void showPasses() {
        System.out.println(UiConstants.PASSES_LIST_TITLE);
        for (Pass pass : passService.getAllPasses()) {
            System.out.println(String.format(UiConstants.PASS_LINE_FORMAT, pass.getId(), pass.getHolderName(),
                    pass.isActive()));
        }
    }

    private class ShowPassesItem implements MenuItem {
        @Override
        public int getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return UiConstants.PASS_MENU_SHOW;
        }

        @Override
        public boolean execute() {
            showPasses();
            return true;
        }
    }

    private class IssuePassItem implements MenuItem {
        @Override
        public int getKey() {
            return 2;
        }

        @Override
        public String getLabel() {
            return UiConstants.PASS_MENU_ISSUE;
        }

        @Override
        public boolean execute() {
            showPasses();
            System.out.println(UiConstants.PROMPT_PASS_ID);
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_PASS_HOLDER);
            String holderName = scanner.nextLine();
            passService.issuePass(id, holderName);
            return true;
        }
    }

    private class DeactivatePassItem implements MenuItem {
        @Override
        public int getKey() {
            return 3;
        }

        @Override
        public String getLabel() {
            return UiConstants.PASS_MENU_DEACTIVATE;
        }

        @Override
        public boolean execute() {
            showPasses();
            System.out.println(UiConstants.PROMPT_PASS_DEACTIVATE);
            int id = Integer.parseInt(scanner.nextLine());
            passService.deactivatePass(id);
            return true;
        }
    }

    private class ShowRoomsForPassItem implements MenuItem {
        @Override
        public int getKey() {
            return 4;
        }

        @Override
        public String getLabel() {
            return UiConstants.PASS_MENU_ROOMS_FOR_PASS;
        }

        @Override
        public boolean execute() {
            System.out.println(UiConstants.PROMPT_PASS_ID);
            int passId = Integer.parseInt(scanner.nextLine());
            List<Room> rooms = bookingService.getRoomsForPass(passId);
            if (rooms.isEmpty()) {
                System.out.println(UiConstants.PASS_NO_BOOKINGS);
                return true;
            }
            System.out.println(UiConstants.PASS_ROOMS_ACCESS_HEADER);
            for (Room room : rooms) {
                System.out.println(String.format(UiConstants.PASS_ROOM_ROW_FORMAT, room.getId(),
                        room.getType().getDisplayName(), room.getName()));
            }
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
            return UiConstants.PASS_MENU_BACK;
        }

        @Override
        public boolean execute() {
            return false;
        }
    }
}
