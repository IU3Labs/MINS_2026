package service;


import model.Car;
import model.Client;
import model.Service;
import repository.CarRepository;
import repository.ServiceRepository;
import repository.impl.InMemoryCarRepository;
import repository.impl.InMemoryServiceRepository;

import java.util.Scanner;

/**
 * - God Class (делает всё: ввод, поиск, расчёт, вывод)
 * - Magic Numbers (0.9, 0.15, 1.1 и т.д.)
 * - Spaghetti Code (смешаны слои)
 * - Hardcoded Dependencies
 * - Copy-Paste (логика копирует часть Appointment.calculateCost)
 */
public class QuickPriceCalculator {

    private final CarRepository carRepository = new InMemoryCarRepository();
    private final ServiceRepository serviceRepository = new InMemoryServiceRepository();

    public void calculateAndPrint() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Госномер авто: ");
        String plate = scanner.nextLine();
        System.out.print("Название услуги: ");
        String serviceName = scanner.nextLine();

        Car car;
        try {
            car = carRepository.findCarByLicensePlate(plate);
        } catch (Exception e) {
            System.out.println("Авто не найдено, считаем без клиента");
            car = null;
        }

        Service service = serviceRepository.findServiceByName(serviceName);
        if (service == null) {
            System.out.println("Услуга не найдена");
            return;
        }

        System.out.print("Тип расчёта (1-обычный, 2-постоянный, 3-срочный, 4-пятничная акция): ");
        int type = scanner.nextInt();

        double price = service.getPrice();
        int visitsCount = 1;

        if (car != null && car.getOwner() != null) {

            visitsCount = 5;
        }

        if (type == 2) {
            if (visitsCount > 1) {
                price = price * 0.95;
            }
        } else if (type == 3) {
            price = price * 1.20;
        } else if (type == 4) {

            price = price * 0.85;
            System.out.println(" Применена пятничная скидка 15% ");
        }

        System.out.println("Примерная стоимость: " + price + " руб.");
        System.out.println("(расчёт выполнен без сохранения записи)");


    }
}