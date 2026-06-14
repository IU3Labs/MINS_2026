package org.example.coreservice;

import lombok.RequiredArgsConstructor;
import org.example.coreservice.ui.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CoreServiceApplication implements CommandLineRunner {

    private final MainMenu mainMenu;

    public static void main(String[] args) {
        SpringApplication.run(CoreServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        mainMenu.start();
        System.exit(0);
    }
}
