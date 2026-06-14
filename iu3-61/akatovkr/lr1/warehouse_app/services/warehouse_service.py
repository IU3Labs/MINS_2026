from abc import ABC, abstractmethod
from typing import List, Tuple

from warehouse_app.domain.compatibility import ICompatibilityPolicy
from warehouse_app.domain.products import Product, product_from_dict
from warehouse_app.exceptions import StockLevelError, UserInputError, ZoneNotFoundError
from warehouse_app.persistence.repository import IWarehouseRepository, WarehouseState
from warehouse_app.services.catalog_service import IProductCatalog

# интерфейс операций склада
class IWarehouseOperations(ABC):
    @abstractmethod
    def list_zones(self) -> List[str]:
        raise NotImplementedError

    @abstractmethod
    def add_stock(self, zone: str, product_id: str, qty: int) -> None:
        raise NotImplementedError

    @abstractmethod
    def remove_stock(self, zone: str, product_id: str, qty: int) -> None:
        raise NotImplementedError

    @abstractmethod
    def move_stock(self, from_zone: str, to_zone: str, product_id: str, qty: int) -> None:
        raise NotImplementedError

    @abstractmethod
    def list_zone(self, zone: str) -> List[Tuple[Product, int]]:
        raise NotImplementedError


class WarehouseService(IWarehouseOperations):
    def __init__(
        self,
        repo: IWarehouseRepository,
        catalog: IProductCatalog,
        policy: ICompatibilityPolicy,
    ):

        # D
        # self._repo = JsonWarehouseRepository("warehouse_data.json")
        # self._catalog = CatalogService(self._repo)
        # self._policy = build_default_compatibility_policy()

        self._repo = repo
        self._catalog = catalog
        self._policy = policy

    # возвращает список зон
    def list_zones(self) -> List[str]:
        return sorted(self._repo.load().zones)

    # добавляет и проводит все проверки
    def add_stock(self, zone: str, product_id: str, qty: int) -> None:
        state, normalized_zone = self._load_and_zone(zone)
        self._ensure_positive_qty(qty, "добавления")

        product = self._catalog.get_product(product_id)

        # O
        # for existing in self._products_in_zone(state, normalized_zone):
        #     if "food" in product.tags and "chemical" in existing.tags:
        #         raise UserInputError("Пищевые товары нельзя хранить с химией")
        #     if "fragile" in product.tags and "heavy" in existing.tags:
        #         raise UserInputError("Хрупкие товары нельзя хранить с тяжёлыми")

        self._policy.ensure_zone_compatible(product, self._products_in_zone(state, normalized_zone))

        state.zones[normalized_zone][product_id] = state.zones[normalized_zone].get(product_id, 0) + qty
        self._repo.save(state)

    def remove_stock(self, zone: str, product_id: str, qty: int) -> None:
        state, normalized_zone = self._load_and_zone(zone)
        self._ensure_positive_qty(qty, "списания")

        current_qty = state.zones[normalized_zone].get(product_id, 0)
        if current_qty < qty:
            raise StockLevelError(
                f"Недостаточно товара в зоне {normalized_zone}. Есть {current_qty}, нужно {qty}"
            )

        self._write_updated_qty(state, normalized_zone, product_id, current_qty - qty)
        self._repo.save(state)

    def move_stock(self, from_zone: str, to_zone: str, product_id: str, qty: int) -> None:
        self._ensure_positive_qty(qty, "перемещения")

        state = self._repo.load()
        source_zone = self._normalize_zone(state, from_zone)
        target_zone = self._normalize_zone(state, to_zone)

        if source_zone == target_zone:
            raise UserInputError("Исходная и целевая зоны должны различаться")

        current_qty = state.zones[source_zone].get(product_id, 0)
        if current_qty < qty:
            raise StockLevelError(
                f"Недостаточно товара в зоне {source_zone}. Есть {current_qty}, нужно {qty}"
            )

        product = self._catalog.get_product(product_id)
        self._policy.ensure_zone_compatible(product, self._products_in_zone(state, target_zone))

        self._write_updated_qty(state, source_zone, product_id, current_qty - qty)
        state.zones[target_zone][product_id] = state.zones[target_zone].get(product_id, 0) + qty
        self._repo.save(state)

    def list_zone(self, zone: str) -> List[Tuple[Product, int]]:
        state, normalized_zone = self._load_and_zone(zone)
        return sorted(
            (
                (self._catalog.get_product(product_id), qty)
                for product_id, qty in state.zones[normalized_zone].items()
            ),
            key=lambda item: item[0].product_id,
        )

    def _load_and_zone(self, zone: str) -> Tuple[WarehouseState, str]:
        state = self._repo.load()
        return state, self._normalize_zone(state, zone)

    def _normalize_zone(self, state: WarehouseState, zone: str) -> str:
        normalized_zone = zone.strip().upper()
        if normalized_zone not in state.zones:
            available = ", ".join(sorted(state.zones))
            raise ZoneNotFoundError(f"Зона '{normalized_zone}' не существует. Доступно: {available}")
        return normalized_zone

    def _products_in_zone(self, state: WarehouseState, zone: str) -> List[Product]:
        products = []
        for product_id in state.zones[zone]:
            raw = state.catalog.get(product_id)
            if raw is not None:
                products.append(product_from_dict(raw))
        return products

    @staticmethod
    def _ensure_positive_qty(qty: int, action_name: str) -> None:
        if qty <= 0:
            raise UserInputError(f"Количество для {action_name} должно быть больше 0")

    @staticmethod
    def _write_updated_qty(state: WarehouseState, zone: str, product_id: str, new_qty: int) -> None:
        if new_qty <= 0:
            state.zones[zone].pop(product_id, None)
        else:
            state.zones[zone][product_id] = new_qty
