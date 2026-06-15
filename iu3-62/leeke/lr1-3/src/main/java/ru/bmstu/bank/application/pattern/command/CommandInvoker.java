package ru.bmstu.bank.application.pattern.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayDeque;
import java.util.Deque;

public class CommandInvoker {
    private static final Logger log = LoggerFactory.getLogger(CommandInvoker.class);

    private final Deque<Command> history = new ArrayDeque<>();
    private final Deque<Command> undoStack = new ArrayDeque<>();

    public void execute(Command command) {
        try {
            log.info("Выполнение: {}", command.getDescription());
            command.execute();
            history.push(command);
            undoStack.clear(); // после новой операции redo невозможен
        } catch (Exception e) {
            log.error("Ошибка выполнения команды: {}", command.getDescription(), e);
            throw e;
        }
    }

    public void undoLast() {
        if (history.isEmpty()) {
            log.warn("История пуста: нечего отменять");
            return;
        }
        Command cmd = history.pop();
        log.info("Отмена: {}", cmd.getDescription());
        cmd.undo();
        undoStack.push(cmd);
    }

    public void redoLast() {
        if (undoStack.isEmpty()) {
            log.warn("Стек отмененных команд пуст");
            return;
        }
        Command cmd = undoStack.pop();
        log.info("Повтор: {}", cmd.getDescription());
        cmd.execute();
        history.push(cmd);
    }

    public Deque<Command> getHistory() { return new ArrayDeque<>(history); }
}