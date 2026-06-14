from pathlib import Path

from warehouse_app.domain.compatibility import (
    build_default_compatibility_policy,
    build_default_rule_factory_registry,
)
from warehouse_app.domain.products import ProductFactory, build_default_kind_registry
from warehouse_app.patterns.commands import WarehouseCommandManager
from warehouse_app.patterns.observers import (
    ConsoleNotificationObserver,
    LowStockObserver,
    OperationLogObserver,
    WarehouseEventDispatcher,
)
from warehouse_app.patterns.sorting import build_default_sort_strategy_registry
from warehouse_app.persistence.repository import JsonWarehouseRepository
from warehouse_app.services.catalog_service import CatalogService
from warehouse_app.services.statistics_service import StatisticsService
from warehouse_app.services.warehouse_service import WarehouseService
from warehouse_app.ui.console_app import ConsoleApp


DEFAULT_RULE_SET = "sanpin"
RULE_SET_MENU = (
    ("1", "sanpin", "СанПиН"),
    ("2", "gost", "ГОСТ"),
    ("3", "internal", "Внутренний регламент"),
)


def _select_rule_set() -> str:
    registry = build_default_rule_factory_registry()
    available_codes = registry.available_codes()
    number_to_code = {number: code for number, code, _ in RULE_SET_MENU}
    code_to_title = {code: title for _, code, title in RULE_SET_MENU}

    print("Выберите набор правил совместимости для текущего запуска:")
    for number, code, title in RULE_SET_MENU:
        default_mark = " (по умолчанию)" if code == DEFAULT_RULE_SET else ""
        print(f"  {number}. {title} [{code}]{default_mark}")
    print("Можно ввести номер пункта или код набора правил.")

    while True:
        raw_value = input("> ").strip().lower()
        if not raw_value:
            selected = DEFAULT_RULE_SET
            print(
                f"Используется набор правил по умолчанию: "
                f"{code_to_title.get(selected, selected)} [{selected}]"
            )
            return selected

        selected = number_to_code.get(raw_value, raw_value)
        if selected in available_codes:
            print(
                f"Выбран набор правил: "
                f"{code_to_title.get(selected, selected)} [{selected}]"
            )
            return selected

        available = ", ".join(available_codes)
        print(
            f"Некорректный выбор. Введите 1, 2, 3 или один из кодов: {available}."
        )


def main() -> None:
    project_root = Path(__file__).resolve().parent
    repository = JsonWarehouseRepository(project_root / "warehouse_data.json")

    kind_registry = build_default_kind_registry()
    sort_registry = build_default_sort_strategy_registry()
    product_factory = ProductFactory(kind_registry)
    selected_rule_set = _select_rule_set()
    compatibility_policy = build_default_compatibility_policy(selected_rule_set)

    catalog = CatalogService(repository)
    event_dispatcher = WarehouseEventDispatcher()
    log_observer = OperationLogObserver()
    low_stock_observer = LowStockObserver(threshold=3)
    event_dispatcher.subscribe(ConsoleNotificationObserver())
    event_dispatcher.subscribe(log_observer)
    event_dispatcher.subscribe(low_stock_observer)

    warehouse = WarehouseService(repository, catalog, compatibility_policy, event_dispatcher)
    statistics = StatisticsService(repository)
    command_manager = WarehouseCommandManager()

    ConsoleApp(
        product_factory=product_factory,
        kind_registry=kind_registry,
        sort_registry=sort_registry,
        command_manager=command_manager,
        log_observer=log_observer,
        low_stock_observer=low_stock_observer,
        catalog=catalog,
        warehouse=warehouse,
        stats=statistics,
    ).run()


if __name__ == "__main__":
    main()
