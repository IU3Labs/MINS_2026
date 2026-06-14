package ru.iu3.ui.items;

import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;
import ru.iu3.util.TestDataUtils;

public class GenerateDataMenuItem implements MenuItem {

    private RoomService roomService;
    private PassService passService;
    private BookingService bookingService;

    public GenerateDataMenuItem(RoomService roomService, PassService passService, BookingService bookingService) {
        this.roomService = roomService;
        this.passService = passService;
        this.bookingService = bookingService;
    }

    @Override
    public int getKey() {
        return 4;
    }

    @Override
    public String getLabel() {
        return UiConstants.MAIN_GENERATE_DATA;
    }

    @Override
    public boolean execute() {
        TestDataUtils.generateDemoData(roomService, passService, bookingService);
        return true;
    }
}
