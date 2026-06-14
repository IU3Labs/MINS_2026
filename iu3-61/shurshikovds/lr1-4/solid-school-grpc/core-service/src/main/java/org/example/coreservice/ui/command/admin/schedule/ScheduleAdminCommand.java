package org.example.solidschool.ui.command.admin.schedule;

public interface ScheduleAdminCommand {
    int key();
    String label();
    void execute();
}
