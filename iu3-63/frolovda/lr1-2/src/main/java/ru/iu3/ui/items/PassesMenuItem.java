package ru.iu3.ui.items;

import ru.iu3.ui.OutputUI;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.handlers.PassesMenuHandler;
import ru.iu3.ui.interfaces.MenuItem;

public class PassesMenuItem implements MenuItem {

    private OutputUI display;
    private PassesMenuHandler passesMenuHandler;

    public PassesMenuItem(OutputUI display, PassesMenuHandler passesMenuHandler) {
        this.display = display;
        this.passesMenuHandler = passesMenuHandler;
    }

    @Override
    public int getKey() {
        return 3;
    }

    @Override
    public String getLabel() {
        return UiConstants.MAIN_PASSES;
    }

    @Override
    public boolean execute() {
        display.showChoice(getLabel());
        passesMenuHandler.run();
        return true;
    }
}
