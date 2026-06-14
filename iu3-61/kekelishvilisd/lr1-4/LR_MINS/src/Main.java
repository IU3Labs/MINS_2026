package ui;

public class MainClient {
    public static void main(String[] args) {
        System.out.println("️  Запуск консольного клиента...");
        try {
            ConsoleUI ui = new ConsoleUI("localhost", 50051);
            ui.start();
        } catch (Exception e) {
            System.err.println("Не удалось запустить клиент: " + e.getMessage());
            e.printStackTrace();
        }
    }
}