package ui;

import exceptions.LibrarySystemException;
import service.LibraryService;

public class DataSeeder {
    public static void seedData(LibraryService library) {
        try {
            library.createPublication(LibraryService.PublicationType.BOOK, "Война и Мир", "Л. Толстой", 1869, "978-1");
            library.createPublication(LibraryService.PublicationType.JOURNAL, "Vogue", "Anna", 2024, "10");
            library.createUser("Иван Иванов", "ivan@email.com");
            library.createUser("Петр Петров", "petr@email.com");
            System.out.println("✅ Тестовые данные загружены");
        } catch (Exception e) { System.err.println("Ошибка seed: " + e.getMessage()); }
    }
}