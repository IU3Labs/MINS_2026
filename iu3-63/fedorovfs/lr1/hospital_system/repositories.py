from abc import ABC, abstractmethod
from typing import List, Dict, Any

from exceptions import HospitalException


class Repository(ABC):
    """Абстракция хранилища данных"""
    # принцип O. open/closed principle. те класс открыт для расширения, но закрыт дял измененеия

    @abstractmethod
    def add(self, item) -> None:
        pass

    @abstractmethod
    def get_by_id(self, item_id: int):
        pass

    @abstractmethod
    def get_all(self) -> List:
        pass

    @abstractmethod
    def update(self, item) -> None:
        pass

    @abstractmethod
    def delete(self, item_id: int) -> None:
        pass


class InMemoryRepository(Repository):
    """Простое хранилище в памяти"""

    def __init__(self) -> None:
        self._storage: Dict[int, Any] = {}

    def add(self, item) -> None:
        if hasattr(item, "id"):
            self._storage[item.id] = item
        else:
            raise HospitalException("Объект должен иметь поле 'id'")

    def get_by_id(self, item_id: int):
        return self._storage.get(item_id)

    def get_all(self) -> List:
        return list(self._storage.values())

    def update(self, item) -> None:
        if hasattr(item, "id"):
            self._storage[item.id] = item
        else:
            raise HospitalException("Объект должен иметь поле 'id' для обновления")

    def delete(self, item_id: int) -> None:
        if item_id in self._storage:
            del self._storage[item_id]


class CustomRepository(Repository):

    def __init__(self) -> None:
        self._storage: Dict[int, Any] = {}

    def add(self, item) -> None:
        if hasattr(item, "id"):
            self._storage[item.id] = item
        else:
            raise HospitalException("Объект должен иметь поле 'id'")

    def get_by_id(self, item_id: int):
        return self._storage.get(item_id)

    def get_all(self) -> bool:
        ...  # нарушен принцип Liskov

    def update(self, item) -> None:
        if hasattr(item, "id"):
            self._storage[item.id] = item
        else:
            raise HospitalException("Объект должен иметь поле 'id' для обновления")

    def delete(self, item_id: int) -> bool:
        ...  # нарушен принцип Liskov


"""
нарушение принципа interface segregation - делаем жирный репозиторий под какой то конкретный сервис
class BigRepository(ABC):

    @abstractmethod
    def add_patient(self, name: str, age: int):

    @abstractmethod
    def add_doctor(self, name: str, age: int): ...

    @abstractmethod
    def add_ward(self, ...): ...

    @abstractmethod
    def schedule_appointment(self, ...): ...

    @abstractmethod
    def print_report(self): ...
    ```"""
