from __future__ import annotations

import json
from pathlib import Path
from typing import Any

from hospital_system.exceptions import StorageError


class JsonStorage:
    """Низкоуровневое хранилище JSON (SRP: только чтение/запись)."""

    def __init__(self, base_dir: Path):
        self._base_dir = base_dir
        self._base_dir.mkdir(parents=True, exist_ok=True)

    def read(self, filename: str) -> Any:
        path = self._base_dir / filename
        if not path.exists():
            return []
        try:
            return json.loads(path.read_text(encoding="utf-8"))
        except Exception as e:
            raise StorageError(f"Не удалось прочитать файл {path}") from e

    def write(self, filename: str, payload: Any) -> None:
        path = self._base_dir / filename
        try:
            path.write_text(
                json.dumps(payload, ensure_ascii=False, indent=2),
                encoding="utf-8",
            )
        except Exception as e:
            raise StorageError(f"Не удалось записать файл {path}") from e