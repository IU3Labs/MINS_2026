from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Callable, Iterable, Tuple

from warehouse_app.domain.products import Product
from warehouse_app.exceptions import CompatibilityError


class ICompatibilityRule(ABC):
    @abstractmethod
    def is_compatible(self, first: Product, second: Product) -> bool:
        raise NotImplementedError

    @abstractmethod
    def description(self) -> str:
        raise NotImplementedError


class ICompatibilityPolicy(ABC):
    @abstractmethod
    def ensure_zone_compatible(self, candidate: Product, products_in_zone: Iterable[Product]) -> None:
        raise NotImplementedError

# L
# class BadCompatibilityPolicy(ICompatibilityPolicy):
#     def ensure_zone_compatible(self, candidate, products_in_zone) -> None:
#         pass


@dataclass(frozen=True)
class SimpleRule(ICompatibilityRule):
    text: str
    predicate: Callable[[Product, Product], bool]

    def is_compatible(self, first: Product, second: Product) -> bool:
        return self.predicate(first, second)

    def description(self) -> str:
        return self.text


class CompatibilityPolicy(ICompatibilityPolicy):
    def __init__(self, rules: Iterable[ICompatibilityRule]):
        self._rules: Tuple[ICompatibilityRule, ...] = tuple(rules)

    def ensure_zone_compatible(self, candidate: Product, products_in_zone: Iterable[Product]) -> None:
        for existing in products_in_zone:
            for rule in self._rules:
                if not rule.is_compatible(candidate, existing):
                    raise CompatibilityError(
                        f"Нарушено товарное соседство: '{candidate.name}' и "
                        f"'{existing.name}'. Правило: {rule.description()}"
                    )


def build_default_compatibility_policy() -> ICompatibilityPolicy:
    rules = [
        SimpleRule(
            "Пищевые товары нельзя хранить вместе с химией",
            lambda a, b: not (("food" in a.tags and "chemical" in b.tags) or ("chemical" in a.tags and "food" in b.tags)),
        ),
        SimpleRule(
            "Хрупкие товары нельзя хранить рядом с тяжёлыми",
            lambda a, b: not (("fragile" in a.tags and "heavy" in b.tags) or ("heavy" in a.tags and "fragile" in b.tags)),
        ),
        SimpleRule(
            "Опасные вещества нельзя хранить рядом с электроникой",
            lambda a, b: not (("hazard" in a.tags and "electronics" in b.tags) or ("electronics" in a.tags and "hazard" in b.tags)),
        ),
    ]
    return CompatibilityPolicy(rules)
