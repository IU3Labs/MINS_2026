from __future__ import annotations

import json
from dataclasses import dataclass
from pathlib import Path
from typing import Dict

from lab4_microservices.common.exceptions import PersistenceError

DEFAULT_ZONE_NAMES = ("A", "B", "C")


@dataclass
class CoreState:
    zones: Dict[str, Dict[str, int]]


class JsonCoreRepository:
    def __init__(self, path: str | Path):
        self._path = Path(path)

    def load(self) -> CoreState:
        if not self._path.exists():
            return CoreState(zones=_default_zones())
        try:
            raw = json.loads(self._path.read_text(encoding="utf-8"))
            return CoreState(zones=_normalize_zones(raw.get("zones")))
        except PersistenceError:
            raise
        except Exception as exc:
            raise PersistenceError(f"Не удалось загрузить складские данные: {exc}") from exc

    def save(self, state: CoreState) -> None:
        try:
            payload = {"zones": _normalize_zones(state.zones)}
            self._path.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
        except Exception as exc:
            raise PersistenceError(f"Не удалось сохранить складские данные: {exc}") from exc


def _default_zones() -> Dict[str, Dict[str, int]]:
    return {zone_name: {} for zone_name in DEFAULT_ZONE_NAMES}


def _normalize_zones(raw_zones: object) -> Dict[str, Dict[str, int]]:
    if raw_zones is None:
        return _default_zones()
    if not isinstance(raw_zones, dict):
        raise PersistenceError("Некорректный формат зон")

    normalized = _default_zones()
    for zone_name, products in raw_zones.items():
        zone_key = str(zone_name).strip().upper()
        normalized.setdefault(zone_key, {})
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
