package org.example.coreservice.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class InputUtil {

    private final Scanner scanner;

    public InputUtil(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Ошибка: введите целое число!");
                scanner.next();
            }
        }
    }
}

