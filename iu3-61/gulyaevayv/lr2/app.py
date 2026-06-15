import logging

from exceptions.pharmacy_exceptions import PharmacyException
from models.sale import Sale
from observers.event_manager import EventManager
from observers.notification_observer import ConsoleNotificationObserver, LogNotificationObserver
from services.pharmacy_service import PharmacyService
from services.sale_service import SaleService
from utils.input_handler import InputHandler


logging.basicConfig(
    filename="app.log",
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
)


class PharmacyApp:
    def __init__(self):
        self.event_manager = EventManager()
        self.event_manager.subscribe(LogNotificationObserver())
        self.event_manager.subscribe(ConsoleNotificationObserver())

        self.pharmacy_service = PharmacyService(event_manager=self.event_manager)
        self.sale_service = SaleService()

    def execute(self, action):
        try:
            if action == "1":
                self.add_medicine()
            elif action == "2":
                self.show_medicines()
            elif action == "3":
                self.search()
            elif action == "4":
                self.sell()
            elif action == "5":
                self.check_expiration()
            elif action == "6":
                self.remove_expired()
            elif action == "7":
                self.show_sales()
            else:
                print("Неизвестная команда")

        except PharmacyException as error:
            print("Ошибка:", error)
            logging.error(str(error))

    def add_medicine(self):
        name = InputHandler.input_string("Название: ")
        manufacturer = InputHandler.input_string("Производитель: ")
        price = InputHandler.input_float("Цена: ")
        expiration_date = InputHandler.input_date("Срок годности (YYYY-MM-DD): ")
        prescription = InputHandler.input_bool("Требуется рецепт? (y/n): ")
        quantity = InputHandler.input_int("Количество: ")

        self.pharmacy_service.add_medicine(
            name, manufacturer, price, expiration_date, prescription, quantity
        )

        print("Добавлено\n")

    def show_medicines(self):
        for medicine in self.pharmacy_service.get_all_medicines():
            print(medicine)

    def search(self):
        keyword = InputHandler.input_string("Введите запрос: ")
        results = self.pharmacy_service.find_medicine(keyword, exact=False)

        for medicine in results:
            print(medicine)

    def sell(self):
        name = InputHandler.input_string("Название: ")
        quantity = InputHandler.input_int("Количество: ")
        prescription = InputHandler.input_bool("Есть рецепт? (y/n): ")

        medicine = self.pharmacy_service.sell_medicine(name, quantity, prescription)
        sale = Sale(name, quantity, medicine.price * quantity, prescription)
        self.sale_service.add_sale(sale)

        print("Продано\n")

    def check_expiration(self):
        expired, soon = self.pharmacy_service.check_expiration_status()

        for medicine in expired:
            print("Просрочено:", medicine)

        for medicine in soon:
            print("Скоро истечет (<30 дней):", medicine)

    def remove_expired(self):
        removed = self.pharmacy_service.remove_expired_medicines()

        for medicine in removed:
            print("Удалено:", medicine)

    def show_sales(self):
        for sale in self.sale_service.get_sales():
            print(sale)

    def save(self):
        self.pharmacy_service.save_medicines()
        self.sale_service.save_sales()
