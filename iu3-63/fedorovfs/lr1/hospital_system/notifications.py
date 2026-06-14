from abc import ABC, abstractmethod
from typing import List


class Observer(ABC):
    """Интерфейс для всех наблюдателей"""
    @abstractmethod
    def update(self, event_type: str, data: str) -> None:
        pass


class ConsoleNotificationObserver(Observer):
    """Выводит уведомления в консоль"""
    def update(self, event_type: str, data: str) -> None:
        print(f"\n🔹 [{event_type}] {data}")


class CloudNotificationObserver(Observer):
    """Отправляет уведомление на почту"""
    def update(self, event_type: str, data: str) -> None:
        print(f"\n📤 [{event_type}] {data}")

    def _send_email(self, event_type: str, data: str):
        ...


class EventManager:
    """Диспетчер событий"""
    def __init__(self):
        self._observers: List[Observer] = []

    def subscribe(self, observer: Observer):
        self._observers.append(observer)

    def notify(self, event_type: str, data: str):
        for observer in self._observers:
            observer.update(event_type, data)