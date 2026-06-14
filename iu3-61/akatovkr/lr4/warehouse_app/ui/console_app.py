from typing import Callable

from warehouse_app.domain.products import ProductFactory, ProductKindRegistry
from warehouse_app.exceptions import EmptyFieldError, NumericFormatError, WarehouseError
from warehouse_app.patterns.commands import (
    AddStockCommand,
    MoveStockCommand,
    RemoveStockCommand,
    WarehouseCommandManager,
)
from warehouse_app.patterns.observers import LowStockObserver, OperationLogObserver
from warehouse_app.patterns.sorting import ProductSortStrategyRegistry
from warehouse_app.services.catalog_service import IProductCatalog
from warehouse_app.services.statistics_service import IStatisticsProvider, WarehouseSummary
from warehouse_app.services.warehouse_service import IWarehouseOperations


class _QuickEstimatorLike:
    def run_console_flow(self) -> None:
        raise NotImplementedError


class ConsoleApp:
    def __init__(
        self,
        product_factory: ProductFactory,
        kind_registry: ProductKindRegistry,
        sort_registry: ProductSortStrategyRegistry,
        command_manager: WarehouseCommandManager,
        log_observer: OperationLogObserver,
        low_stock_observer: LowStockObserver,
        catalog: IProductCatalog,
        warehouse: IWarehouseOperations,
        stats: IStatisticsProvider,
        quick_estimator: _QuickEstimatorLike,
    ):
        self._product_factory = product_factory
        self._kind_registry = kind_registry
        self._sort_registry = sort_registry
        self._command_manager = command_manager
        self._log_observer = log_observer
        self._low_stock_observer = low_stock_observer
        self._catalog = catalog
        self._warehouse = warehouse
        self._stats = stats
        self._quick_estimator = quick_estimator
        self._actions: dict[str, Callable[[], None]] = {
            "1": self._cmd_add_product,
            "2": self._cmd_list_products,
            "3": self._cmd_add_stock,
            "4": self._cmd_remove_stock,
            "5": self._cmd_move_stock,
            "6": self._cmd_list_zone,
            "7": self._cmd_stats,
            "8": self._cmd_show_log,
            "9": self._cmd_undo_last,
            "10": self._cmd_show_alerts,
            "11": self._cmd_quick_estimate,
        }

    def run(self) -> None:
        while True:
            self._print_menu()
            command = input("Выберите действие: ").strip()

            if command == "0":
                print("Выход из программы.")
                return

            action = self._actions.get(command)
            if action is None:
                print("Неизвестная команда.")
                continue

            try:
                action()
            except WarehouseError as exc:
                print(f"[ОШИБКА] {exc}")
            except Exception as exc:
                print(f"[НЕПРЕДВИДЕННАЯ ОШИБКА] {exc}")

    def _print_menu(self) -> None:
        print("\n=== Складская информационная система ===")
        print("1) Добавить товар в каталог")
        print("2) Показать каталог товаров (Strategy)")
        print("3) Добавить остаток в зону (Command)")
        print("4) Списать остаток из зоны (Command)")
        print("5) Переместить товар между зонами (Command)")
        print("6) Показать содержимое зоны")
        print("7) Показать статистику")
        print("8) Показать журнал операций (Observer)")
        print("9) Отменить последнюю складскую команду (Command)")
        print("10) Показать предупреждения о малом остатке (Observer)")
        print("11) Быстрый расчёт примерной стоимости заказа (ЛР3 / God Class)")
        print("0) Выход")

    def _cmd_add_product(self) -> None:
        print(f"Доступные типы: {self._kind_registry.describe_available()}")
        print("Можно указать несколько типов через запятую, например: electronics,fragile")

        product = self._product_factory.create(
            product_id=self._ask_text("ID товара"),
            name=self._ask_text("Название"),
            unit_price=self._ask_float("Цена за единицу"),
            kind_codes=self._ask_csv("Типы товара"),
        )
        self._catalog.add_product(product)
        print("Товар добавлен в каталог.")

    def _cmd_list_products(self) -> None:
        print(f"Доступные стратегии сортировки: {self._sort_registry.describe_available()}")
        raw_strategy = input("Код стратегии [id]: ").strip() or self._sort_registry.default().code
        strategy = self._sort_registry.get(raw_strategy)
        products = self._catalog.list_products_sorted(strategy)
        if not products:
            print("Каталог пуст.")
            return

        print(f"\nКаталог товаров (сортировка {strategy.title}):")
        for product in products:
            print(
                f"- ID={product.product_id}; название={product.name}; "
                f"цена={product.unit_price:.2f}; теги={', '.join(sorted(product.tags))}"
            )

    def _cmd_add_stock(self) -> None:
        command = AddStockCommand(
            warehouse=self._warehouse,
            zone=self._ask_zone("Зона"),
            product_id=self._ask_text("ID товара"),
            qty=self._ask_int("Количество"),
        )
        self._command_manager.execute(command)
        print("Остаток добавлен.")

    def _cmd_remove_stock(self) -> None:
        command = RemoveStockCommand(
            warehouse=self._warehouse,
            zone=self._ask_zone("Зона"),
            product_id=self._ask_text("ID товара"),
            qty=self._ask_int("Количество"),
        )
        self._command_manager.execute(command)
        print("Остаток списан.")

    def _cmd_move_stock(self) -> None:
        command = MoveStockCommand(
            warehouse=self._warehouse,
            from_zone=self._ask_zone("Из зоны"),
            to_zone=self._ask_zone("В зону"),
            product_id=self._ask_text("ID товара"),
            qty=self._ask_int("Количество"),
        )
        self._command_manager.execute(command)
        print("Перемещение выполнено.")

    def _cmd_list_zone(self) -> None:
        zone = self._ask_zone("Зона")
        items = self._warehouse.list_zone(zone)
        if not items:
            print("Зона пуста.")
            return

        print(f"\nСодержимое зоны {zone.upper()}:")
        for product, qty in items:
            print(
                f"- ID={product.product_id}; название={product.name}; "
                f"количество={qty}; теги={', '.join(sorted(product.tags))}"
            )

    def _cmd_stats(self) -> None:
        self._print_summary(self._stats.summary())

    def _cmd_show_log(self) -> None:
        entries = self._log_observer.entries()
        if not entries:
            print("Журнал операций пуст.")
            return

        print("\nЖурнал операций:")
        for entry in entries:
            print(f"- {entry}")

    def _cmd_undo_last(self) -> None:
        description = self._command_manager.undo_last()
        print(f"Отмена выполнена для команды: {description}")

    def _cmd_quick_estimate(self) -> None:
        self._quick_estimator.run_console_flow()

    def _cmd_show_alerts(self) -> None:
        alerts = self._low_stock_observer.alerts()
        if not alerts:
            print("Предупреждений о малом остатке пока нет.")
            return

        print("\nПредупреждения о малом остатке:")
        for alert in alerts:
            print(f"- {alert}")

    def _print_summary(self, summary: WarehouseSummary) -> None:
        print("\nОбщая статистика:")
        print(f"Всего единиц товара: {summary.total_units}")
        print(f"Всего складских позиций: {summary.total_positions}")
        print(f"Общая стоимость товаров: {summary.total_value:.2f}")

        print("\nСтатистика по зонам:")
        for zone_name, zone_stats in summary.by_zone.items():
            print(
                f"- {zone_name}: единиц={zone_stats.units}, позиций={zone_stats.positions}, "
                f"стоимость={zone_stats.value:.2f}"
            )

        print("\nСтатистика по тегам:")
        if not summary.by_tag:
            print("- Нет данных")
        else:
            for tag, units in summary.by_tag.items():
                print(f"- {tag}: {units}")

        print("\nСтатистика по товарам:")
        if not summary.by_product:
            print("- Нет данных")
        else:
            for product_id, product_stats in summary.by_product.items():
                print(
                    f"- ID={product_id}; название={product_stats.name}; "
                    f"единиц={product_stats.units}; стоимость={product_stats.value:.2f}"
                )

    def _ask_zone(self, caption: str) -> str:
        zones = ", ".join(self._warehouse.list_zones())
        return self._ask_text(f"{caption} ({zones})")

    @staticmethod
    def _ask_text(caption: str) -> str:
        value = input(f"{caption}: ").strip()
        if not value:
            raise EmptyFieldError(f"Поле '{caption}' не может быть пустым")
        return value

    @staticmethod
    def _ask_int(caption: str) -> int:
        raw = input(f"{caption}: ").strip()
        try:
            return int(raw)
        except ValueError as exc:
            raise NumericFormatError(f"Поле '{caption}' должно быть целым числом") from exc

    @staticmethod
    def _ask_float(caption: str) -> float:
        raw = input(f"{caption}: ").strip().replace(",", ".")
        try:
            return float(raw)
        except ValueError as exc:
            raise NumericFormatError(f"Поле '{caption}' должно быть числом") from exc

    @staticmethod
    def _ask_csv(caption: str) -> list[str]:
        raw = input(f"{caption}: ").strip()
        if not raw:
            raise EmptyFieldError(f"Поле '{caption}' не может быть пустым")
        return [item.strip() for item in raw.split(",") if item.strip()]
