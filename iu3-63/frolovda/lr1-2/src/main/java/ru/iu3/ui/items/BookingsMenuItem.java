package ru.iu3.ui.items;

import ru.iu3.ui.OutputUI;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.handlers.BookingsMenuHandler;
import ru.iu3.ui.interfaces.MenuItem;

public class BookingsMenuItem implements MenuItem {

    private OutputUI display;
    private BookingsMenuHandler bookingsMenuHandler;

    public BookingsMenuItem(OutputUI display, BookingsMenuHandler bookingsMenuHandler) {
        this.display = display;
        this.bookingsMenuHandler = bookingsMenuHandler;
    }

    @Override
    public int getKey() {
        return 2;
    }

    @Override
    public String getLabel() {
        return UiConstants.MAIN_BOOKINGS;
    }

    @Override
    public boolean execute() {
        display.showChoice(getLabel());
        bookingsMenuHandler.run();
        return true;
    }
}
