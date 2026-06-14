package ru.iu3.ui.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;

public class RoomsMenuHandler {

    private Scanner scanner;
    private RoomService roomService;
    private List<MenuItem> items = new ArrayList<>();

    public RoomsMenuHandler(Scanner scanner, RoomService roomService) {
        this.scanner = scanner;
        this.roomService = roomService;
        items.add(new ShowRoomsItem());
        items.add(new AddWorkplaceItem());
        items.add(new AddMeetingRoomItem());
        items.add(new DeleteRoomItem());
        items.add(new BackItem());
    }

    public void run() {
        boolean inRoomsMenu = true;
        while (inRoomsMenu) {
            for (MenuItem item : items) {
                System.out.println(item.getKey() + ". " + item.getLabel());
            }
            try {
                System.out.print(UiConstants.PROMPT_MENU);
                int choice = Integer.parseInt(scanner.nextLine());
                MenuItem selected = findItem(choice);
                if (selected != null) {
                    inRoomsMenu = selected.execute();
                } else {
                    System.out.println(UiConstants.INVALID_CHOICE);
                }
            } catch (Exception e) {
                System.out.println(UiConstants.ERROR_PREFIX + e.getMessage());
            }
        }
    }

    public void showRooms() {
        System.out.println(UiConstants.ROOMS_LIST_TITLE);
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println(UiConstants.ROOMS_EMPTY);
            return;
        }

        System.out.println(UiConstants.ROOMS_TABLE_HEADER);
        for (Room room : rooms) {
            if (room.getIsLocked()) {
                System.out.println(String.format(UiConstants.ROOM_ROW_FORMAT, room.getId(),
                        room.getType().getDisplayName(), room.getName(), room.getHourlyRate())
                        + UiConstants.ROOM_LOCKED_SUFFIX);
                continue;
            }
            System.out.println(String.format(UiConstants.ROOM_ROW_FORMAT, room.getId(),
                    room.getType().getDisplayName(), room.getName(), room.getHourlyRate()));
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

    private class ShowRoomsItem implements MenuItem {
        @Override
        public int getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return UiConstants.ROOM_MENU_SHOW;
        }

        @Override
        public boolean execute() {
            showRooms();
            return true;
        }
    }

    private class AddWorkplaceItem implements MenuItem {
        @Override
        public int getKey() {
            return 2;
        }

        @Override
        public String getLabel() {
            return UiConstants.ROOM_MENU_ADD_WORKPLACE;
        }

        @Override
        public boolean execute() {
            System.out.println(UiConstants.PROMPT_WORKPLACE_ID);
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_WORKPLACE_NAME);
            String name = scanner.nextLine();
            System.out.println(UiConstants.PROMPT_HOURLY_RATE);
            int hourlyRate = Integer.parseInt(scanner.nextLine());
            roomService.addRoom(RoomEnum.WORKPLACE, id, name, hourlyRate);
            return true;
        }
    }

    private class AddMeetingRoomItem implements MenuItem {
        @Override
        public int getKey() {
            return 3;
        }

        @Override
        public String getLabel() {
            return UiConstants.ROOM_MENU_ADD_MEETING;
        }

        @Override
        public boolean execute() {
            System.out.println(UiConstants.PROMPT_MEETING_ID);
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println(UiConstants.PROMPT_MEETING_NAME);
            String name = scanner.nextLine();
            System.out.println(UiConstants.PROMPT_HOURLY_RATE);
            int hourlyRate = Integer.parseInt(scanner.nextLine());
            roomService.addRoom(RoomEnum.MEETING_ROOM, id, name, hourlyRate);
            return true;
        }
    }

    private class DeleteRoomItem implements MenuItem {
        @Override
        public int getKey() {
            return 4;
        }

        @Override
        public String getLabel() {
            return UiConstants.ROOM_MENU_LOCK;
        }

        @Override
        public boolean execute() {
            showRooms();
            System.out.println(UiConstants.PROMPT_LOCK_ROOM_ID);
            int id = Integer.parseInt(scanner.nextLine());
            roomService.lockRoom(id);
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
            return UiConstants.ROOM_MENU_BACK;
        }

        @Override
        public boolean execute() {
            return false;
        }
    }
}
