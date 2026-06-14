from __future__ import annotations

from decimal import Decimal

from cafe_app.exceptions import (
    CafeError,
    CategoryAlreadyExistsError,
    CategoryNotFoundError,
    ItemNotFoundError,
    OrderAlreadyOpenForTableError,
    OrderClosedError,
    OrderNotFoundError,
    PaymentMethodNotSupportedError,
    ValidationError,
)
from cafe_app.payment import PaymentFactory
from cafe_app.receipt import ReceiptService
from cafe_app.repositories import InMemoryMenuRepository, InMemoryOrderRepository
from cafe_app.services import CheckoutService, MenuService, OrderService
from cafe_app.utils import parse_money


def ask_int(prompt: str) -> int: #безопасно вводит номер стола, id стола и блюда, количество
    raw = input(prompt).strip()
    try:
        value = int(raw)
    except ValueError as e:
        raise ValidationError("Ожидается целое число.") from e
    return value


def ask_str(prompt: str) -> str: #прочитать текст и убрать пробелы
    return input(prompt).strip()


def ask_money(prompt: str) -> Decimal:#функция читает деньги
    raw = ask_str(prompt)
    return parse_money(raw)


def ask_payment_method(prompt: str) -> str:#Чтение способа оплаты
    return ask_str(prompt).lower()#приводит к нижнему ркгистру


def main() -> None:
    menu_repo = InMemoryMenuRepository() #хранит меню
    order_repo = InMemoryOrderRepository() #хранит заказы

    menu_service = MenuService(repo=menu_repo) #создаёт категорию, добавялет блюдо, показывает меню
    order_service = OrderService(menu_repo=menu_repo, order_repo=order_repo) #работает с заказами
    checkout_service = CheckoutService( #работает с оплатой
        order_repo=order_repo,
        payment_factory=PaymentFactory(),
        receipt_service=ReceiptService(),
    )

    while True:
        print("\n=== Кафе (вариант 10) ===")
        print("1. Добавить категорию")
        print("2. Добавить блюдо")
        print("3. Показать меню")
        print("4. Открыть заказ на столе")
        print("5. Добавить блюдо в заказ (по столу)")
        print("6. Показать текущий заказ (по столу)")
        print("7. Оплатить заказ и распечатать чек (по столу)")
        print("0. Выход")

        choice = ask_str("Выберите пункт: ")

        try:
            if choice == "1":
                name = ask_str("Название категории: ")
                category = menu_service.create_category(name) #создает категорию
                print(f"Категория создана: [{category.id}] {category.name}")

            elif choice == "2":
                category_id = ask_int("ID категории: ")
                name = ask_str("Название блюда: ")
                price_text = ask_str("Цена (например 199.50): ")
                item = menu_service.create_item(category_id, name, price_text)
                print(f"Блюдо создано: [{item.id}] {item.name} = {item.price:.2f}")

            elif choice == "3":
                print(menu_service.show_menu())

            elif choice == "4":
                table_number = ask_int("Номер стола: ")
                order = order_service.start_order(table_number=table_number)
                print(f"Открыт заказ: №{order.id} для стола {order.table_number}")

            elif choice == "5":
                table_number = ask_int("Номер стола: ")
                order = order_service.get_open_order_by_table(table_number)
                item_id = ask_int("ID блюда: ")
                quantity = ask_int("Количество: ")
                order_service.add_item_to_order(order_id=order.id, item_id=item_id, quantity=quantity)
                print("Блюдо добавлено.")

            elif choice == "6":
                table_number = ask_int("Номер стола: ")
                order = order_service.get_open_order_by_table(table_number)
                print(order_service.show_order_text(order_id=order.id))

            elif choice == "7":
                table_number = ask_int("Номер стола: ")
                order = order_service.get_open_order_by_table(table_number)
                method = ask_payment_method("Способ оплаты (cash/card/online): ")
                receipt_text = checkout_service.checkout(order_id=order.id, payment_method_key=method)
                print(receipt_text)

            elif choice == "0":
                print("Выход.")
                return

            else:
                raise ValidationError("Неизвестный пункт меню.")

        except CafeError as e: #ловит собственные ошибки (блюдо не найдено...)
            print(f"Ошибка: {e}")
        except ValueError as e: #ловит обычные ошибки типо неправильное число
            print(f"Некорректные данные: {e}")


if __name__ == "__main__":
    main()

