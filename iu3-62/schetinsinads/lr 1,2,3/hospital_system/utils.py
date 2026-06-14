from __future__ import annotations

from datetime import date, datetime
from uuid import uuid4

from hospital_system.exceptions import ValidationError

DATE_FMT = "%Y-%m-%d"
DT_FMT = "%Y-%m-%d %H:%M"


def new_id() -> str:
    return uuid4().hex


def parse_date(s: str) -> date:
    try:
        return datetime.strptime(s.strip(), DATE_FMT).date()
    except Exception as e:
        raise ValidationError(
            f"Неверный формат даты. Ожидалось {DATE_FMT}. Пример: 2026-03-04"
        ) from e


def parse_datetime(s: str) -> datetime:
    try:
        return datetime.strptime(s.strip(), DT_FMT)
    except Exception as e:
        raise ValidationError(
            f"Неверный формат даты-времени. Ожидалось {DT_FMT}. Пример: 2026-03-04 18:30"
        ) from e


def ensure_non_empty(value: str, field_name: str) -> str:
    v = (value or "").strip()
    if not v:
        raise ValidationError(f"Поле '{field_name}' не может быть пустым")
    return v


def ensure_positive_int(value: str, field_name: str) -> int:
    try:
        n = int(value)
    except Exception as e:
        raise ValidationError(f"Поле '{field_name}' должно быть целым числом") from e
    if n <= 0:
        raise ValidationError(f"Поле '{field_name}' должно быть > 0")
    return n


def today() -> date:
    return date.today()