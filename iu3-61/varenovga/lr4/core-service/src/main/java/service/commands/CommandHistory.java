package service.commands;

import java.util.ArrayList;
import java.util.List;
public class CommandHistory {
    private final List<Command> history = new ArrayList<>();
    private int currentIndex = -1;

    public void push(Command command) {
        while (history.size() > currentIndex + 1) {
            history.remove(history.size() - 1);
        }
        history.add(command);
        currentIndex++;
    }

    public void undo() {
        if (currentIndex >= 0) {
            Command command = history.get(currentIndex);
            command.undo();
            currentIndex--;
        }
    }

    public void showHistory() {
        System.out.println("\n📋 ИСТОРИЯ ОПЕРАЦИЙ: ");
        if (history.isEmpty()) {
            System.out.println("   История пуста");
        } else {
            for (int i = 0; i < history.size(); i++) {
                String marker = (i <= currentIndex) ? "✓ " : "↩ ";
                System.out.println("   " + (i + 1) + ". " + marker + "  " +
                        history.get(i).getDescription());
            }
        }
    }

    public int size() {
        return history.size();
    }
}