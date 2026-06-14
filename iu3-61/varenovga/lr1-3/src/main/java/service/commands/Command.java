package main.java.service.commands;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}