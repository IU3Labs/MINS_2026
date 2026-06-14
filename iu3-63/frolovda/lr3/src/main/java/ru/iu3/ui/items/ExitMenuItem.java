package ru.iu3.ui.items;

import ru.iu3.ui.MenuRunnerHelper;
import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;

public class ExitMenuItem implements MenuItem {

    private MenuRunnerHelper display;

    public ExitMenuItem(MenuRunnerHelper display) {
        this.display = display;
    }

    @Override
    public int getKey() {
        return 0;
    }

    @Override
    public String getLabel() {
        return UiConstants.MAIN_EXIT;
    }

    @Override
    public boolean execute() {
        display.showGoodbye();
        return false;
    }
}
