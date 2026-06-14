package org.example.coreservice.ui.command.admin;

public interface AdminCommand {
    int key();
    String label();
    void execute();
}
