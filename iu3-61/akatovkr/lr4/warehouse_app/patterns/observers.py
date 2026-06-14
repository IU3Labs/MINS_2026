from __future__ import annotations

from abc import ABC, abstractmethod
from dataclasses import dataclass, field
from typing import Any, Iterable


@dataclass(frozen=True)
class WarehouseEvent:
    event_type: str
    message: str
    payload: dict[str, Any] = field(default_factory=dict)


class IWarehouseObserver(ABC):
    @abstractmethod
    def update(self, event: WarehouseEvent) -> None:
        raise NotImplementedError


class IWarehouseEventPublisher(ABC):
    @abstractmethod
    def subscribe(self, observer: IWarehouseObserver) -> None:
        raise NotImplementedError

    @abstractmethod
    def unsubscribe(self, observer: IWarehouseObserver) -> None:
        raise NotImplementedError

    @abstractmethod
    def notify(self, event: WarehouseEvent) -> None:
        raise NotImplementedError


class WarehouseEventDispatcher(IWarehouseEventPublisher):
    def __init__(self) -> None:
        self._observers: list[IWarehouseObserver] = []

    def subscribe(self, observer: IWarehouseObserver) -> None:
        if observer not in self._observers:
            self._observers.append(observer)

    def unsubscribe(self, observer: IWarehouseObserver) -> None:
        if observer in self._observers:
            self._observers.remove(observer)

    def notify(self, event: WarehouseEvent) -> None:
        for observer in tuple(self._observers):
            observer.update(event)


class ConsoleNotificationObserver(IWarehouseObserver):
    def update(self, event: WarehouseEvent) -> None:
        print(f"[СОБЫТИЕ] {event.message}")


class OperationLogObserver(IWarehouseObserver):
    def __init__(self) -> None:
        self._entries: list[str] = []

    def update(self, event: WarehouseEvent) -> None:
        self._entries.append(f"{event.event_type}: {event.message}")

    def entries(self) -> list[str]:
        return list(self._entries)


class LowStockObserver(IWarehouseObserver):
    def __init__(self, threshold: int = 3) -> None:
        self._threshold = threshold
        self._alerts: list[str] = []

    def update(self, event: WarehouseEvent) -> None:
        for level in self._extract_levels(event.payload.get("levels")):
            if level["qty"] <= self._threshold:
                alert = (
                    f"Малый остаток: товар {level['product_id']} в зоне {level['zone']} "
                    f"имеет количество {level['qty']}"
                )
                self._alerts.append(alert)
                print(f"[ПРЕДУПРЕЖДЕНИЕ] {alert}")

    def alerts(self) -> list[str]:
        return list(self._alerts)

    @staticmethod
    def _extract_levels(levels: Any) -> Iterable[dict[str, Any]]:
        if not isinstance(levels, list):
            return ()
        return [level for level in levels if isinstance(level, dict)]
