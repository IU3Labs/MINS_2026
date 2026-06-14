from typing import Callable

from warehouse_app.domain.products import ProductFactory, ProductKindRegistry
from warehouse_app.exceptions import EmptyFieldError, NumericFormatError, WarehouseError
from warehouse_app.services.catalog_service import IProductCatalog
from warehouse_app.services.statistics_service import IStatisticsProvider, WarehouseSummary
from warehouse_app.services.warehouse_service import IWarehouseOperations


class ConsoleApp:
    def __init__(
        self,
        product_factory: ProductFactory,
        kind_registry: ProductKindRegistry,
        catalog: IProductCatalog,
        warehouse: IWarehouseOperations,
        stats: IStatisticsProvider,
    ):
        self._product_factory = product_factory  # создание словаря команд
        self._kind_registry = kind_registry
        self._catalog = catalog
        self._warehouse = warehouse
        self._stats = stats
        self._actions: dict[str, Callable[[], None]] = {
            "1": self._cmd_add_product,
            "2": self._cmd_list_products,
            "3": self._cmd_add_stock,
            "4": self._cmd_remove_stock,
            "5": self._cmd_move_stock,
            "6": self._cmd_list_zone,
            "7": self._cmd_stats,
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
        print("=== Складская информационная система ===")
        print("1) Добавить товар в каталог")
        print("2) Показать каталог товаров")
        print("3) Добавить остаток в зону")
        print("4) Списать остаток из зоны")
        print("5) Переместить товар между зонами")
        print("6) Показать содержимое зоны")
        print("7) Показать статистику")
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
        products = self._catalog.list_products()
        if not products:
            print("Каталог пуст.")
            return

        print("Каталог товаров:")
        for product in products:
            print(
                f"- ID={product.product_id}; название={product.name}; "
                f"цена={product.unit_price:.2f}; теги={', '.join(sorted(product.tags))}"
            )

    def _cmd_add_stock(self) -> None:
        self._warehouse.add_stock(
            zone=self._ask_zone("Зона"),
            product_id=self._ask_text("ID товара"),
            qty=self._ask_int("Количество"),
        )
        print("Остаток добавлен.")

    def _cmd_remove_stock(self) -> None:
        self._warehouse.remove_stock(
            zone=self._ask_zone("Зона"),
            product_id=self._ask_text("ID товара"),
            qty=self._ask_int("Количество"),
        )
        print("Остаток списан.")

    def _cmd_move_stock(self) -> None:
        self._warehouse.move_stock(
            from_zone=self._ask_zone("Из зоны"),
            to_zone=self._ask_zone("В зону"),
            product_id=self._ask_text("ID товара"),
            qty=self._ask_int("Количество"),
        )
        print("Перемещение выполнено.")

    def _cmd_list_zone(self) -> None:
        zone = self._ask_zone("Зона")
        items = self._warehouse.list_zone(zone)
        if not items:
            print("Зона пуста.")
            return

        print(f"Содержимое зоны {zone.upper()}:")
        for product, qty in items:
            print(
                f"- ID={product.product_id}; название={product.name}; "
                f"количество={qty}; теги={', '.join(sorted(product.tags))}"
            )

    def _cmd_stats(self) -> None:
        self._print_summary(self._stats.summary())

    def _print_summary(self, summary: WarehouseSummary) -> None:
        print("Общая статистика:")
        print(f"Всего единиц товара: {summary.total_units}")
        print(f"Всего складских позиций: {summary.total_positions}")
        print(f"Общая стоимость товаров: {summary.total_value:.2f}")

        print("Статистика по зонам:")
        for zone_name, zone_stats in summary.by_zone.items():
            print(
                f"- {zone_name}: единиц={zone_stats.units}, позиций={zone_stats.positions}, "
                f"стоимость={zone_stats.value:.2f}"
            )

        print("Статистика по тегам:")
        if not summary.by_tag:
            print("- Нет данных")
        else:
            for tag, units in summary.by_tag.items():
                print(f"- {tag}: {units}")

        print("Статистика по товарам:")
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
