package org.example.coreservice.ui.elements;

import org.example.coreservice.entity.user.User;
import org.example.coreservice.exception.BadCredentialsException;
import org.example.coreservice.exception.UserNotFoundException;
import org.example.coreservice.service.AuthService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AuthConsoleUI extends BaseConsoleUI {

    private final AuthService authService;

    public AuthConsoleUI(AuthService authService, Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.authService = authService;
    }

    public User promptLogin() {
        while (true) {
            System.out.println("\n=== Добро пожаловать ===");
            System.out.print("Логин: ");
            String username = scanner.next();

            System.out.print("Пароль: ");
            String password = scanner.next();

            try {
                return authService.login(username, password);
            } catch (UserNotFoundException e) {
                System.out.println("Такого пользователя не существует");
            } catch (BadCredentialsException e) {
                System.out.println("Неверный пароль");
            }
        }
    }
}
