from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Dict

from warehouse_app.domain.products import product_from_dict
from warehouse_app.persistence.repository import IWarehouseRepository

# сбор статистики по складу

# структуры для результата статистики
@dataclass(frozen=True)
class ZoneStats:
    units: int
    positions: int
    value: float


@dataclass(frozen=True)
class ProductStats:
    name: str
    units: int
    value: float


@dataclass(frozen=True)
class WarehouseSummary:
    total_units: int
    total_positions: int
    total_value: float
    by_zone: Dict[str, ZoneStats]
    by_tag: Dict[str, int]
    by_product: Dict[str, ProductStats]


class IStatisticsProvider(ABC):
    @abstractmethod
    def summary(self) -> WarehouseSummary:
        raise NotImplementedError


class StatisticsService(IStatisticsProvider):
    def __init__(self, repo: IWarehouseRepository):
        self._repo = repo

    def summary(self) -> WarehouseSummary:
        state = self._repo.load()
        catalog = {product_id: product_from_dict(data) for product_id, data in state.catalog.items()}

        total_units = 0
        total_positions = 0
        total_value = 0.0
        by_zone: Dict[str, ZoneStats] = {}
        by_tag: Dict[str, int] = {}
        by_product: Dict[str, ProductStats] = {}

        product_units: Dict[str, int] = {}
        product_values: Dict[str, float] = {}

        for zone_name, items in state.zones.items():
            zone_units = sum(items.values())
            zone_positions = len(items)
            zone_value = 0.0

            for product_id, qty in items.items():
                product = catalog.get(product_id)
                if product is None:
                    continue

                item_value = product.unit_price * qty
                zone_value += item_value
                total_value += item_value

                product_units[product_id] = product_units.get(product_id, 0) + qty
                product_values[product_id] = product_values.get(product_id, 0.0) + item_value

                for tag in product.tags:
                    by_tag[tag] = by_tag.get(tag, 0) + qty

            total_units += zone_units
            total_positions += zone_positions
            by_zone[zone_name] = ZoneStats(zone_units, zone_positions, round(zone_value, 2))

        for product_id, units in sorted(product_units.items()):
            product = catalog[product_id]
            by_product[product_id] = ProductStats(
                name=product.name,
                units=units,
                value=round(product_values[product_id], 2),
            )

        return WarehouseSummary(
            total_units=total_units,
            total_positions=total_positions,
            total_value=round(total_value, 2),
            by_zone=by_zone,
            by_tag=dict(sorted(by_tag.items())),
            by_product=by_product,
        )
