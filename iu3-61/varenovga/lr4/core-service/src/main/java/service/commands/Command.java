package service.commands;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}