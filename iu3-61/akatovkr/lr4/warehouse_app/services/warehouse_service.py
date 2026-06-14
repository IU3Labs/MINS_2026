from abc import ABC, abstractmethod
from typing import List, Tuple

from warehouse_app.domain.compatibility import ICompatibilityPolicy
from warehouse_app.domain.products import Product, product_from_dict
from warehouse_app.exceptions import StockLevelError, UserInputError, ZoneNotFoundError
from warehouse_app.patterns.observers import IWarehouseEventPublisher, WarehouseEvent
from warehouse_app.persistence.repository import IWarehouseRepository, WarehouseState
from warehouse_app.services.catalog_service import IProductCatalog


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
        event_publisher: IWarehouseEventPublisher | None = None,
    ):
        self._repo = repo
        self._catalog = catalog
        self._policy = policy
        self._event_publisher = event_publisher

    def list_zones(self) -> List[str]:
        return sorted(self._repo.load().zones)

    def add_stock(self, zone: str, product_id: str, qty: int) -> None:
        state, normalized_zone = self._load_and_zone(zone)
        self._ensure_positive_qty(qty, "добавления")

        product = self._catalog.get_product(product_id)
        self._policy.ensure_zone_compatible(product, self._products_in_zone(state, normalized_zone))

        new_qty = state.zones[normalized_zone].get(product_id, 0) + qty
        state.zones[normalized_zone][product_id] = new_qty
        self._repo.save(state)

        self._publish(
            event_type="stock_added",
            message=(
                f"В зону {normalized_zone} добавлено {qty} ед. товара {product_id}. "
                f"Текущий остаток: {new_qty}"
            ),
            levels=[{"zone": normalized_zone, "product_id": product_id, "qty": new_qty}],
        )

    def remove_stock(self, zone: str, product_id: str, qty: int) -> None:
        state, normalized_zone = self._load_and_zone(zone)
        self._ensure_positive_qty(qty, "списания")

        current_qty = state.zones[normalized_zone].get(product_id, 0)
        if current_qty < qty:
            raise StockLevelError(
                f"Недостаточно товара в зоне {normalized_zone}. Есть {current_qty}, нужно {qty}"
            )

        new_qty = current_qty - qty
        self._write_updated_qty(state, normalized_zone, product_id, new_qty)
        self._repo.save(state)

        self._publish(
            event_type="stock_removed",
            message=(
                f"Из зоны {normalized_zone} списано {qty} ед. товара {product_id}. "
                f"Текущий остаток: {new_qty}"
            ),
            levels=[{"zone": normalized_zone, "product_id": product_id, "qty": new_qty}],
        )

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

        source_new_qty = current_qty - qty
        target_new_qty = state.zones[target_zone].get(product_id, 0) + qty

        self._write_updated_qty(state, source_zone, product_id, source_new_qty)
        state.zones[target_zone][product_id] = target_new_qty
        self._repo.save(state)

        self._publish(
            event_type="stock_moved",
            message=(
                f"Товар {product_id} перемещён: {qty} ед. из зоны {source_zone} в зону {target_zone}."
            ),
            levels=[
                {"zone": source_zone, "product_id": product_id, "qty": source_new_qty},
                {"zone": target_zone, "product_id": product_id, "qty": target_new_qty},
            ],
        )

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

    def _publish(self, event_type: str, message: str, levels: list[dict[str, int | str]]) -> None:
        if self._event_publisher is None:
            return
        self._event_publisher.notify(
            WarehouseEvent(event_type=event_type, message=message, payload={"levels": levels})
        )

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
