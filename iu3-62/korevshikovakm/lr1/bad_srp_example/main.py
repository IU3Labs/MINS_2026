from __future__ import annotations

from datetime import datetime

from cafe_app.entities import OrderLine
from cafe_app.exceptions import CafeError, ValidationError
from cafe_app.payment import PaymentFactory
from cafe_app.receipt import ReceiptService
from cafe_app.repositories import InMemoryMenuRepository, InMemoryOrderRepository
from cafe_app.utils import parse_money


class CafeSystemGodClass:
    """ одновременно отвечает за:
    - ввод/вывод в консоли,
    - бизнес-логику меню и заказов,
    - оплату,
    - формирование чека.
    """

    def __init__(self) -> None:
        self.menu_repo = InMemoryMenuRepository()
        self.order_repo = InMemoryOrderRepository()
        self.payment_factory = PaymentFactory()
        self.receipt_service = ReceiptService()

    def ask_int(self, prompt: str) -> int:
        raw = input(prompt).strip()
        try:
            return int(raw)
        except ValueError as e:
            raise ValidationError("Ожидается целое число.") from e

    def ask_str(self, prompt: str) -> str:
        return input(prompt).strip()

    def add_category(self) -> None:
        name = self.ask_str("Название категории: ")
        category = self.menu_repo.add_category(name)
        print(f"Категория создана: [{category.id}] {category.name}")

    def add_item(self) -> None:
        category_id = self.ask_int("ID категории: ")
        name = self.ask_str("Название блюда: ")
        price_text = self.ask_str("Цена (например 199.50): ")
        price = parse_money(price_text)
        item = self.menu_repo.add_item(category_id=category_id, name=name, price=price)
        print(f"Блюдо создано: [{item.id}] {item.name} = {item.price:.2f}")

    def show_menu(self) -> None:
        categories = self.menu_repo.list_categories()
        print("====== МЕНЮ ======")
        for category in categories:
            print(f"[{category.id}] {category.name}")
            for item in self.menu_repo.list_items(category.id):
                print(f"  - [{item.id}] {item.name}: {item.price:.2f}")
        print("====================")

    def open_order(self) -> None:
        table_number = self.ask_int("Номер стола: ")
        order = self.order_repo.create_order(table_number=table_number)
        print(f"Открыт заказ: №{order.id} для стола {order.table_number}")

    def add_item_to_order(self) -> None:
        table_number = self.ask_int("Номер стола: ")
        order = self.order_repo.get_open_order_by_table(table_number)
        item_id = self.ask_int("ID блюда: ")
        quantity = self.ask_int("Количество: ")
        if quantity <= 0:
            raise ValidationError("Количество должно быть положительным.")
        item = self.menu_repo.get_item(item_id)
        line = OrderLine(item_id=item.id, item_name=item.name, unit_price=item.price, quantity=quantity)
        self.order_repo.add_line(order_id=order.id, line=line)
        print("Блюдо добавлено.")

    def show_order(self) -> None:
        table_number = self.ask_int("Номер стола: ")
        order = self.order_repo.get_open_order_by_table(table_number)
        print("====== ТЕКУЩИЙ ЗАКАЗ ======")
        print(f"Заказ №: {order.id} | Стол: {order.table_number} | Статус: {order.status}")
        print("------------------------------------")
        if not order.lines:
            print("(пусто)")
        for line in order.lines:
            print(f"{line.quantity} x {line.item_name} @ {line.unit_price:.2f} = {line.line_total:.2f}")
        print("------------------------------------")
        print(f"Сумма: {order.total_amount():.2f}")
        print("===============================")

    def checkout(self) -> None:
        table_number = self.ask_int("Номер стола: ")
        order = self.order_repo.get_open_order_by_table(table_number)
        method = self.ask_str("Способ оплаты (cash/card/online): ").lower()
        processor = self.payment_factory.get_processor(method)
        payment = processor.pay(order.total_amount())
        self.order_repo.mark_paid(order_id=order.id, payment_method=payment.method_name, paid_at=datetime.now())
        receipt_text = self.receipt_service.generate_receipt_text(order, payment)
        print(receipt_text)

    def run(self) -> None:
        while True:
            print("\n=== Кафе (плохой SRP пример) ===")
            print("1. Добавить категорию")
            print("2. Добавить блюдо")
            print("3. Показать меню")
            print("4. Открыть заказ на столе")
            print("5. Добавить блюдо в заказ (по столу)")
            print("6. Показать текущий заказ (по столу)")
            print("7. Оплатить заказ и распечатать чек (по столу)")
            print("0. Выход")
            choice = self.ask_str("Выберите пункт: ")

            try:
                if choice == "1":
                    self.add_category()
                elif choice == "2":
                    self.add_item()
                elif choice == "3":
                    self.show_menu()
                elif choice == "4":
                    self.open_order()
                elif choice == "5":
                    self.add_item_to_order()
                elif choice == "6":
                    self.show_order()
                elif choice == "7":
                    self.checkout()
                elif choice == "0":
                    print("Выход.")
                    return
                else:
                    raise ValidationError("Неизвестный пункт меню.")
            except CafeError as e:
                print(f"Ошибка: {e}")
            except ValueError as e:
                print(f"Некорректные данные: {e}")


def main() -> None:
    app = CafeSystemGodClass()
    app.run()


if __name__ == "__main__":
    main()
