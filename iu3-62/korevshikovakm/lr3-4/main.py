from __future__ import annotations

import logging
import os
import uuid

import grpc

from cafe_app.commands import CommandBus, CreateCategoryCommand, CreateMenuItemCommand
from cafe_app.discounts import DiscountFactory
from cafe_app.exceptions import CafeError, ReferenceServiceUnavailableError, ValidationError
from cafe_app.grpc_errors import (
    format_local_error_message,
    format_user_error_message,
    grpc_status_name,
    log_rpc_failure_on_client,
)
from cafe_app.trace_logging import TRACE_ID_CTX
from cafe_app.grpc_menu_client import GrpcMenuRepository
from cafe_app.observer import InMemoryOrderLogObserver, OrderEventPublisher
from cafe_app.payment import PaymentFactory
from cafe_app.quick_calc_spaghetti import run_quick_price_calculator
from cafe_app.receipt import ReceiptService
from cafe_app.repositories import InMemoryOrderRepository
from cafe_app.services import CheckoutService, MenuService, OrderService
from cafe_app.trace_logging import setup_logging, trace_scope


def ask_int(prompt: str) -> int: #безопасно вводит номер стола, id стола и блюда, количество
    raw = input(prompt).strip()
    try:
        value = int(raw)
    except ValueError as e:
        raise ValidationError("Ожидается целое число.") from e
    return value


def ask_str(prompt: str) -> str: #прочитать текст и убрать пробелы
    return input(prompt).strip()


def ask_payment_method(prompt: str) -> str:#Чтение способа оплаты
    return ask_str(prompt).lower()#приводит к нижнему ркгистру


def main() -> None:
    setup_logging("service-a") #настраиваются логи для сервиса А
    log = logging.getLogger("cafe.core") #Логгер, через него сервис А пишет сообщения в лог

    reference_target = os.environ.get("CAFE_REFERENCE_GRPC", "localhost:50051") #Сервис А узнает адрес В
    channel = grpc.insecure_channel(reference_target) #Создаётся gRPC-канал до Service B
    print(f"\n Сервис A: меню через gRPC → {reference_target}")
    print("Запустите сервис B: python run_reference_server.py \n")

    menu_repo = GrpcMenuRepository(channel) #Создаётся репозиторий меню, который работает через gRPC, Service A вызывать методы меню через сеть
    order_repo = InMemoryOrderRepository()

    events = OrderEventPublisher()
    log_observer = InMemoryOrderLogObserver()
    events.subscribe(log_observer)

    menu_service = MenuService(repo=menu_repo) #Создаётся сервис меню, передаётся GrpcMenuRepository
    command_bus = CommandBus()
    order_service = OrderService(menu_repo=menu_repo, order_repo=order_repo, publisher=events)
    checkout_service = CheckoutService(
        order_repo=order_repo,
        payment_factory=PaymentFactory(),
        receipt_service=ReceiptService(),
        discount_factory=DiscountFactory(),
        publisher=events,
    )

    try:
        while True:
            print("\n=== Кафе (вариант 10, микросервисы) ===")
            print("1. Добавить категорию")
            print("2. Добавить блюдо")
            print("3. Показать меню")
            print("4. Открыть заказ на столе")
            print("5. Добавить блюдо в заказ (по столу)")
            print("6. Показать текущий заказ (по столу)")
            print("7. Оплатить заказ и распечатать чек (по столу)")
            print("8. Показать журнал событий заказа (Observer)")
            print("9. Показать историю команд (Command)")
            print("10. Быстрый калькулятор (ЛР3, Spaghetti Code)")
            print("0. Выход")

            choice = ask_str("Выберите пункт: ")

            if choice == "0":
                print("Выход.")
                return

            with trace_scope(str(uuid.uuid4())): #На каждое действие пользователя создаётся новый уникальный Trace ID
                log.info("Действие пользователя: menu_choice=%s", choice) #Service A записывает в лог, какой пункт меню выбрал пользователь.
                try:
                    if choice == "1":
                        name = ask_str("Название категории: ")
                        category = command_bus.execute(
                            CreateCategoryCommand(menu_service=menu_service, name=name) #категория создаётся не в Service A, а в Service B. МенюСервис - ГрпсМенюРепозиторий - грпс - сервис в
                        )
                        print(f"Категория создана: [{category.id}] {category.name}")

                    elif choice == "2":
                        category_id = ask_int("ID категории: ")
                        name = ask_str("Название блюда: ")
                        price_text = ask_str("Цена (например 199.50): ")
                        item = command_bus.execute(
                            CreateMenuItemCommand(
                                menu_service=menu_service,
                                category_id=category_id,
                                name=name,
                                price_text=price_text,
                            )
                        )
                        print(f"Блюдо создано: [{item.id}] {item.name} = {item.price:.2f}")

                    elif choice == "3":
                        print(menu_service.show_menu()) # MenuService.show_menu() - GrpcMenuRepository.list_categories() - gRPC ListMenu

                    elif choice == "4":
                        table_number = ask_int("Номер стола: ")
                        order = order_service.start_order(table_number=table_number)
                        print(f"Открыт заказ: №{order.id} для стола {order.table_number}")

                    elif choice == "5":
                        table_number = ask_int("Номер стола: ")
                        order = order_service.get_open_order_by_table(table_number) #Service A делает gRPC-вызов в Service B, получает название и цену блюда
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
                        print("Тип скидки: none / student / happy")
                        discount_key = ask_str("Введите скидку: ").lower()
                        receipt_text = checkout_service.checkout(
                            order_id=order.id,
                            payment_method_key=method,
                            discount_key=discount_key,
                        )
                        print(receipt_text)

                    elif choice == "8":
                        if not log_observer.events:
                            print("Журнал событий пуст.")
                        else:
                            print("=== События ===")
                            for event in log_observer.events:
                                print(event)

                    elif choice == "9":
                        if not command_bus.history:
                            print("История команд пуста.")
                        else:
                            print("=== История команд ===")
                            for cmd_name in command_bus.history:
                                print(cmd_name)

                    elif choice == "10":
                        run_quick_price_calculator()

                    else:
                        raise ValidationError("Неизвестный пункт меню.")

                except CafeError as e:
                    print(format_user_error_message(e))
                except grpc.RpcError as e:
                    log_rpc_failure_on_client(e, rpc="(меню)")
                    err = ReferenceServiceUnavailableError(
                        "Справочник меню (сервис B) временно недоступен. "
                        "Запустите сервер справочника (run_reference_server.py) и проверьте адрес подключения.",
                        grpc_status=grpc_status_name(e),
                        trace_id=TRACE_ID_CTX.get(),
                    )
                    print(format_user_error_message(err))
                except ValueError as e:
                    print(format_local_error_message(e))
    finally:
        channel.close() #gRPC-канал закрывается


if __name__ == "__main__":
    main()
