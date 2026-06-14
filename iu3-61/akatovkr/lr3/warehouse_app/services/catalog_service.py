from abc import ABC, abstractmethod
from typing import List

from warehouse_app.domain.products import Product, product_from_dict, product_to_dict
from warehouse_app.exceptions import DuplicateProductError, ProductNotFoundError
from warehouse_app.patterns.sorting import IProductSortStrategy
from warehouse_app.persistence.repository import IWarehouseRepository


class IProductCatalog(ABC):
    @abstractmethod
    def add_product(self, product: Product) -> None:
        raise NotImplementedError

    @abstractmethod
    def get_product(self, product_id: str) -> Product:
        raise NotImplementedError

    @abstractmethod
    def list_products(self) -> List[Product]:
        raise NotImplementedError

    @abstractmethod
    def list_products_sorted(self, strategy: IProductSortStrategy) -> List[Product]:
        raise NotImplementedError


class CatalogService(IProductCatalog):
    def __init__(self, repo: IWarehouseRepository):
        self._repo = repo

    def add_product(self, product: Product) -> None:
        state = self._repo.load()
        if product.product_id in state.catalog:
            raise DuplicateProductError(f"Товар с ID={product.product_id} уже существует")
        state.catalog[product.product_id] = product_to_dict(product)
        self._repo.save(state)

    def get_product(self, product_id: str) -> Product:
        state = self._repo.load()
        raw = state.catalog.get(product_id)
        if raw is None:
            raise ProductNotFoundError(f"Товар с ID={product_id} не найден")
        return product_from_dict(raw)

    def list_products(self) -> List[Product]:
        state = self._repo.load()
        return sorted(
            (product_from_dict(product_data) for product_data in state.catalog.values()),
            key=lambda product: product.product_id,
        )

    def list_products_sorted(self, strategy: IProductSortStrategy) -> List[Product]:
        return strategy.sort(self.list_products())
