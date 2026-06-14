import json
from abc import ABC, abstractmethod
from dataclasses import dataclass
from pathlib import Path
from typing import Dict

from warehouse_app.exceptions import PersistenceError

DEFAULT_ZONE_NAMES = ("A", "B", "C")


@dataclass
class WarehouseState:
    zones: Dict[str, Dict[str, int]]
    catalog: Dict[str, Dict]


class IWarehouseRepository(ABC):
    @abstractmethod
    def load(self) -> WarehouseState:
        raise NotImplementedError

    @abstractmethod
    def save(self, state: WarehouseState) -> None:
        raise NotImplementedError


class JsonWarehouseRepository(IWarehouseRepository):
    def __init__(self, path: str | Path):
        self._path = Path(path)

    def load(self) -> WarehouseState:
        if not self._path.exists():
            return WarehouseState(zones=_default_zones(), catalog={})

        try:
            raw = json.loads(self._path.read_text(encoding="utf-8"))
            return WarehouseState(
                zones=_normalize_zones(raw.get("zones")),
                catalog=_normalize_catalog(raw.get("catalog")),
            )
        except PersistenceError:
            raise
        except Exception as exc:
            raise PersistenceError(f"Не удалось загрузить данные: {exc}") from exc

    def save(self, state: WarehouseState) -> None:
        try:
            payload = {
                "zones": _normalize_zones(state.zones),
                "catalog": _normalize_catalog(state.catalog),
            }
            self._path.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
        except PersistenceError:
            raise
        except Exception as exc:
            raise PersistenceError(f"Не удалось сохранить данные: {exc}") from exc


def _default_zones() -> Dict[str, Dict[str, int]]:
    return {zone_name: {} for zone_name in DEFAULT_ZONE_NAMES}


def _normalize_zones(raw_zones: object) -> Dict[str, Dict[str, int]]:
    if raw_zones is None:
        return _default_zones()
    if not isinstance(raw_zones, dict):
        raise PersistenceError("Некорректный формат зон в файле данных")

    normalized = _default_zones()
    for zone_name, products in raw_zones.items():
        zone_key = str(zone_name).strip().upper()
        if zone_key not in normalized:
            normalized[zone_key] = {}
        if not isinstance(products, dict):
            raise PersistenceError(f"Некорректный формат содержимого зоны {zone_key}")
        for product_id, qty in products.items():
            amount = int(qty)
            if amount < 0:
                raise PersistenceError(
                    f"Количество товара {product_id} в зоне {zone_key} не может быть отрицательным"
                )
            if amount > 0:
                normalized[zone_key][str(product_id)] = amount
    return normalized


def _normalize_catalog(raw_catalog: object) -> Dict[str, Dict]:
    if raw_catalog is None:
        return {}
    if not isinstance(raw_catalog, dict):
        raise PersistenceError("Некорректный формат каталога в файле данных")
    return {str(product_id): dict(product_data) for product_id, product_data in raw_catalog.items()}
