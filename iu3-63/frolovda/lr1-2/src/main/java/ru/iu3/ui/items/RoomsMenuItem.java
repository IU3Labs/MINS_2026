package ru.iu3.ui.items;

import ru.iu3.ui.OutputUI;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.handlers.RoomsMenuHandler;
import ru.iu3.ui.interfaces.MenuItem;

public class RoomsMenuItem implements MenuItem {

    private OutputUI display;
    private RoomsMenuHandler roomsMenuHandler;

    public RoomsMenuItem(OutputUI display, RoomsMenuHandler roomsMenuHandler) {
        this.display = display;
        this.roomsMenuHandler = roomsMenuHandler;
    }

    @Override
    public int getKey() {
        return 1;
    }

    @Override
    public String getLabel() {
        return UiConstants.MAIN_ROOMS;
    }

    @Override
    public boolean execute() {
        display.showChoice(getLabel());
        roomsMenuHandler.run();
        return true;
    }
}
