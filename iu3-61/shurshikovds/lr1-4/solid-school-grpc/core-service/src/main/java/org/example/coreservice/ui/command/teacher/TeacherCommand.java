package org.example.coreservice.ui.command.teacher;

import org.example.coreservice.entity.user.TeacherUser;

public interface TeacherCommand {
    int key();
    String label();
    void execute(TeacherUser teacher);
}
