package org.example.coreservice.ui;

import lombok.RequiredArgsConstructor;
import org.example.coreservice.entity.enums.UserRole;
import org.example.coreservice.entity.user.StudentUser;
import org.example.coreservice.entity.user.TeacherUser;
import org.example.coreservice.entity.user.User;
import org.example.coreservice.exception.UnknownRoleException;
import org.example.coreservice.ui.elements.AdminConsoleUI;
import org.example.coreservice.ui.elements.AuthConsoleUI;
import org.example.coreservice.ui.elements.StudentConsoleUI;
import org.example.coreservice.ui.elements.TeacherConsoleUI;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainMenu {

    private final StudentConsoleUI studentUI;
    private final TeacherConsoleUI teacherUI;
    private final AdminConsoleUI adminUI;
    private final AuthConsoleUI authUI;

    //entry point
    public void start() {
        User user = authUI.promptLogin();

        if (user instanceof StudentUser s) {
            studentUI.showMenu(s);
        } else if (user instanceof TeacherUser t) {
            teacherUI.showMenu(t);
        } else if (user.getRole() == UserRole.ADMIN) {
            adminUI.showMenu();
        } else {
            throw new UnknownRoleException(user.getRole());
        }
    }
}