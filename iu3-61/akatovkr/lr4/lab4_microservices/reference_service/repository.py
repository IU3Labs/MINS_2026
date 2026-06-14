from __future__ import annotations

import json
from dataclasses import dataclass
from pathlib import Path
from typing import Dict

from lab4_microservices.common.exceptions import PersistenceError


@dataclass
class ReferenceState:
    catalog: Dict[str, Dict]


class JsonReferenceRepository:
    def __init__(self, path: str | Path):
        self._path = Path(path)

    def load(self) -> ReferenceState:
        if not self._path.exists():
            return ReferenceState(catalog={})
        try:
            raw = json.loads(self._path.read_text(encoding="utf-8"))
            catalog = raw.get("catalog", {})
            if not isinstance(catalog, dict):
                raise PersistenceError("Некорректный формат каталога")
            return ReferenceState(catalog={str(k): dict(v) for k, v in catalog.items()})
        except PersistenceError:
            raise
        except Exception as exc:
            raise PersistenceError(f"Не удалось загрузить справочные данные: {exc}") from exc

    def save(self, state: ReferenceState) -> None:
        try:
            payload = {"catalog": state.catalog}
            self._path.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
        except Exception as exc:
            raise PersistenceError(f"Не удалось сохранить справочные данные: {exc}") from exc
