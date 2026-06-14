package ru.iu3.ui;

import java.util.List;

import ru.iu3.ui.constants.UiConstants;
import ru.iu3.ui.interfaces.MenuItem;

public class OutputUI {

    public void showWelcome() {
        System.out.println(UiConstants.WELCOME_MESSAGE);
    }

    public void showOptions(List<MenuItem> items) {
        for (MenuItem item : items) {
            System.out.println(item.getKey() + ". " + item.getLabel());
        }
    }

    public void showPrompt() {
        System.out.print(UiConstants.PROMPT_MENU);
    }

    public void showChoice(String choice) {
        System.out.println(String.format(UiConstants.CHOICE_MESSAGE, choice));
    }

    public void showInvalidChoice() {
        System.out.println(UiConstants.INVALID_CHOICE);
    }

    public void showGoodbye() {
        System.out.println(UiConstants.GOODBYE_MESSAGE);
    }

    public void showError(String message) {
        System.out.println(UiConstants.ERROR_PREFIX + message);
    }
}
