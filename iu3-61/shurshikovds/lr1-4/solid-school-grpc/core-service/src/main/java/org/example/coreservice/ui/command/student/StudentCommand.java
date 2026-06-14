package org.example.coreservice.ui.command.student;


import org.example.coreservice.entity.user.StudentUser;

public interface StudentCommand {
    int key();
    String label();
    void execute(StudentUser student);
}
