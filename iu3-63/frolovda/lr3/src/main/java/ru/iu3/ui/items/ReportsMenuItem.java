package ru.iu3.ui.items;

import ru.iu3.ui.MenuRunnerHelper;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.handlers.ReportsMenuHandler;
import ru.iu3.ui.interfaces.MenuItem;

public class ReportsMenuItem implements MenuItem {

    private MenuRunnerHelper display;
    private ReportsMenuHandler reportsMenuHandler;

    public ReportsMenuItem(MenuRunnerHelper display, ReportsMenuHandler reportsMenuHandler) {
        this.display = display;
        this.reportsMenuHandler = reportsMenuHandler;
    }

    @Override
    public int getKey() {
        return 5;
    }

    @Override
    public String getLabel() {
        return UiConstants.MAIN_REPORTS;
    }

    @Override
    public boolean execute() {
        display.showChoice(getLabel());
        reportsMenuHandler.run();
        return true;
    }
}
