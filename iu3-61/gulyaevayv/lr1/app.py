import logging

from services.pharmacy_service import PharmacyService
from services.sale_service import SaleService
from models.sale import Sale
from utils.input_handler import InputHandler
from exceptions.pharmacy_exceptions import PharmacyException


logging.basicConfig(
    filename="app.log",
    level=logging.ERROR,
    format="%(asctime)s - %(levelname)s - %(message)s"
)


class PharmacyApp:

    def __init__(self):
        self.pharmacy_service = PharmacyService()
        self.sale_service = SaleService()

    def execute(self, action):

        try:

            if action == "1":
                self._add_medicine()

            elif action == "2":
                self._show_medicines()

            elif action == "3":
                self._search()

            elif action == "4":
                self._sell()

            elif action == "5":
                self._check_expiration()

            elif action == "6":
                self._remove_expired()

            elif action == "7":
                self._show_sales()

        except PharmacyException as e:
            print("Ошибка:", e)
            logging.error(str(e))

    def _add_medicine(self):

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

    def _show_medicines(self):

        for m in self.pharmacy_service.get_all_medicines():
            print(m)

    def _search(self):

        keyword = InputHandler.input_string("Введите запрос: ")

        results = self.pharmacy_service.find_medicine(keyword, exact=False)

        for m in results:
            print(m)

    def _sell(self):

        name = InputHandler.input_string("Название: ")
        quantity = InputHandler.input_int("Количество: ")
        prescription = InputHandler.input_bool("Есть рецепт? (y/n): ")

        medicine = self.pharmacy_service.sell_medicine(name, quantity, prescription)

        sale = Sale(name, quantity, medicine.price * quantity, prescription)

        self.sale_service.add_sale(sale)

        print("Продано\n")

    def _check_expiration(self):

        expired, soon = self.pharmacy_service.check_expiration_status()

        for m in expired:
            print("Просрочено:", m)

        for m in soon:
            print("Скоро истечет (<30 дней):", m)

    def _remove_expired(self):

        removed = self.pharmacy_service.remove_expired_medicines()

        for m in removed:
            print("Удалено:", m)

    def _show_sales(self):

        for s in self.sale_service.get_sales():
            print(s)

    def save(self):

        self.pharmacy_service.save_medicines()
        self.sale_service.save_sales()
