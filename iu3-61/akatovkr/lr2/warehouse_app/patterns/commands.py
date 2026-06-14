from __future__ import annotations

from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import List

from warehouse_app.exceptions import CommandHistoryError
from warehouse_app.services.warehouse_service import IWarehouseOperations


class ICommand(ABC):
    @property
    @abstractmethod
    def description(self) -> str:
        raise NotImplementedError

    @abstractmethod
    def execute(self) -> None:
        raise NotImplementedError

    @abstractmethod
    def undo(self) -> None:
        raise NotImplementedError


@dataclass(frozen=True)
class AddStockCommand(ICommand):
    warehouse: IWarehouseOperations
    zone: str
    product_id: str
    qty: int

    @property
    def description(self) -> str:
        return f"Добавить {self.qty} ед. товара {self.product_id} в зону {self.zone}"

    def execute(self) -> None:
        self.warehouse.add_stock(self.zone, self.product_id, self.qty)

    def undo(self) -> None:
        self.warehouse.remove_stock(self.zone, self.product_id, self.qty)


@dataclass(frozen=True)
class RemoveStockCommand(ICommand):
    warehouse: IWarehouseOperations
    zone: str
    product_id: str
    qty: int

    @property
    def description(self) -> str:
        return f"Списать {self.qty} ед. товара {self.product_id} из зоны {self.zone}"

    def execute(self) -> None:
        self.warehouse.remove_stock(self.zone, self.product_id, self.qty)

    def undo(self) -> None:
        self.warehouse.add_stock(self.zone, self.product_id, self.qty)


@dataclass(frozen=True)
class MoveStockCommand(ICommand):
    warehouse: IWarehouseOperations
    from_zone: str
    to_zone: str
    product_id: str
    qty: int

    @property
    def description(self) -> str:
        return (
            f"Переместить {self.qty} ед. товара {self.product_id} "
            f"из зоны {self.from_zone} в зону {self.to_zone}"
        )

    def execute(self) -> None:
        self.warehouse.move_stock(self.from_zone, self.to_zone, self.product_id, self.qty)

    def undo(self) -> None:
        self.warehouse.move_stock(self.to_zone, self.from_zone, self.product_id, self.qty)


class WarehouseCommandManager:
    def __init__(self) -> None:
        self._history: List[ICommand] = []

    def execute(self, command: ICommand) -> None:
        command.execute()
        self._history.append(command)

    def undo_last(self) -> str:
        if not self._history:
            raise CommandHistoryError("История команд пуста: отменять нечего")

        command = self._history.pop()
        command.undo()
        return command.description

    def history(self) -> list[str]:
        return [command.description for command in reversed(self._history)]
