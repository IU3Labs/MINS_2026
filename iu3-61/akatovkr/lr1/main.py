from pathlib import Path

from warehouse_app.domain.compatibility import build_default_compatibility_policy
from warehouse_app.domain.products import ProductFactory, build_default_kind_registry
from warehouse_app.persistence.repository import JsonWarehouseRepository
from warehouse_app.services.catalog_service import CatalogService
from warehouse_app.services.statistics_service import StatisticsService
from warehouse_app.services.warehouse_service import WarehouseService
from warehouse_app.ui.console_app import ConsoleApp


def main() -> None:
    project_root = Path(__file__).resolve().parent # путь до main.py, чтобы потом корректно найти JSON

    # создает объект, который работает с JSON как с хранилищем данных
    repository = JsonWarehouseRepository(project_root / "warehouse_data.json")

    kind_registry = build_default_kind_registry() # реестр видов товаров
    product_factory = ProductFactory(kind_registry) # фабрика которая по типу товара создает нужный объект товара
    compatibility_policy = build_default_compatibility_policy() # правила товарного соседства

    catalog = CatalogService(repository)
    warehouse = WarehouseService(repository, catalog, compatibility_policy)
    statistics = StatisticsService(repository)

    ConsoleApp(product_factory, kind_registry, catalog, warehouse, statistics).run()


if __name__ == "__main__":
    main()











# S — Single Responsibility Principle
# Один класс должен отвечать за одну задачу.

# O — Open/Closed Principle
# Код должен быть открыт для расширения, но закрыт для изменения.

# L — Liskov Substitution Principle
# Наследника или другую реализацию можно подставить вместо базового класса без поломки логики.

# I — Interface Segregation Principle
# Лучше много маленьких интерфейсов, чем один большой и неудобный.

# D — Dependency Inversion Principle
# Зависеть нужно от абстракций, а не от конкретных реализаций.