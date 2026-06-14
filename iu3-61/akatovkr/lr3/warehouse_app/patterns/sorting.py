from __future__ import annotations

from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Iterable

from warehouse_app.domain.products import Product
from warehouse_app.exceptions import UnknownSortStrategyError


class IProductSortStrategy(ABC):
    @property
    @abstractmethod
    def code(self) -> str:
        raise NotImplementedError

    @property
    @abstractmethod
    def title(self) -> str:
        raise NotImplementedError

    @abstractmethod
    def sort(self, products: Iterable[Product]) -> list[Product]:
        raise NotImplementedError


@dataclass(frozen=True)
class _BaseSortStrategy(IProductSortStrategy):
    _code: str
    _title: str

    @property
    def code(self) -> str:
        return self._code

    @property
    def title(self) -> str:
        return self._title


class SortByIdStrategy(_BaseSortStrategy):
    def __init__(self) -> None:
        super().__init__("id", "по ID")

    def sort(self, products: Iterable[Product]) -> list[Product]:
        return sorted(products, key=lambda product: product.product_id)


class SortByNameStrategy(_BaseSortStrategy):
    def __init__(self) -> None:
        super().__init__("name", "по названию")

    def sort(self, products: Iterable[Product]) -> list[Product]:
        return sorted(products, key=lambda product: product.name.lower())


class SortByPriceStrategy(_BaseSortStrategy):
    def __init__(self) -> None:
        super().__init__("price", "по цене")

    def sort(self, products: Iterable[Product]) -> list[Product]:
        return sorted(products, key=lambda product: (product.unit_price, product.product_id))


class SortByTagCountStrategy(_BaseSortStrategy):
    def __init__(self) -> None:
        super().__init__("tags", "по числу тегов")

    def sort(self, products: Iterable[Product]) -> list[Product]:
        return sorted(products, key=lambda product: (len(product.tags), product.product_id))


class ProductSortStrategyRegistry:
    def __init__(self, strategies: Iterable[IProductSortStrategy]):
        prepared = {strategy.code.lower(): strategy for strategy in strategies}
        if not prepared:
            raise UnknownSortStrategyError("Реестр стратегий сортировки не может быть пустым")
        self._strategies = prepared

    def get(self, code: str) -> IProductSortStrategy:
        strategy = self._strategies.get(code.strip().lower())
        if strategy is None:
            raise UnknownSortStrategyError(
                f"Неизвестная стратегия сортировки '{code}'. Доступно: {self.describe_available()}"
            )
        return strategy

    def default(self) -> IProductSortStrategy:
        return self._strategies["id"]

    def describe_available(self) -> str:
        return ", ".join(
            f"{code} ({strategy.title})" for code, strategy in sorted(self._strategies.items())
        )


def build_default_sort_strategy_registry() -> ProductSortStrategyRegistry:
    return ProductSortStrategyRegistry(
        [
            SortByIdStrategy(),
            SortByNameStrategy(),
            SortByPriceStrategy(),
            SortByTagCountStrategy(),
        ]
    )
