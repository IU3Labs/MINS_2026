package command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<ParkingCommand> history = new ArrayList<>();

    public boolean execute(ParkingCommand cmd) {
        try {
            cmd.execute();
            history.add(cmd);
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка выполнения команды: " + e.getMessage());
            return false;
        }
    }

    public void printLog() {
        if (history.isEmpty()) {
            System.out.println("Журнал операций пуст.");
            return;
        }
        System.out.println("\n=== ЖУРНАЛ ОПЕРАЦИЙ ===");
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, history.get(i).getLogEntry());
        }
    }

    public int getHistorySize() {
        return history.size();
    }
}