from dataclasses import dataclass
from typing import Dict, FrozenSet, Iterable

from lab4_microservices.common.exceptions import EmptyFieldError, UnknownProductKindError, UserInputError


@dataclass(frozen=True)
class Product:
    product_id: str
    name: str
    unit_price: float
    tags: FrozenSet[str]

    def __post_init__(self) -> None:
        if not self.product_id.strip():
            raise EmptyFieldError("ID товара не может быть пустым")
        if not self.name.strip():
            raise EmptyFieldError("Название товара не может быть пустым")
        if self.unit_price < 0:
            raise UserInputError("Цена товара не может быть отрицательной")
        if not self.tags:
            raise UserInputError("У товара должен быть хотя бы один тип/признак")


@dataclass(frozen=True)
class ProductKind:
    code: str
    title: str
    tags: FrozenSet[str]


class ProductKindRegistry:
    def __init__(self, kinds: Iterable[ProductKind]):
        prepared = {kind.code.lower(): kind for kind in kinds}
        if not prepared:
            raise UserInputError("Реестр типов товаров не может быть пустым")
        self._kinds = prepared

    def available_codes(self) -> list[str]:
        return sorted(self._kinds)

    def describe_available(self) -> str:
        return ", ".join(f"{code} ({self._kinds[code].title})" for code in self.available_codes())

    def resolve_tags(self, kind_codes: Iterable[str]) -> FrozenSet[str]:
        normalized_codes = [code.strip().lower() for code in kind_codes if code.strip()]
        if not normalized_codes:
            raise UserInputError("Нужно указать хотя бы один тип товара")

        unknown_codes = [code for code in normalized_codes if code not in self._kinds]
        if unknown_codes:
            available = ", ".join(self.available_codes())
            raise UnknownProductKindError(
                f"Неизвестные типы товара: {', '.join(sorted(unknown_codes))}. Доступно: {available}"
            )

        resolved_tags = set()
        for code in normalized_codes:
            resolved_tags.update(self._kinds[code].tags)
        return frozenset(resolved_tags)


class ProductFactory:
    def __init__(self, registry: ProductKindRegistry):
        self._registry = registry

    def create(self, product_id: str, name: str, unit_price: float, kind_codes: Iterable[str]) -> Product:
        return Product(
            product_id=product_id,
            name=name,
            unit_price=unit_price,
            tags=self._registry.resolve_tags(kind_codes),
        )


def product_to_dict(product: Product) -> Dict:
    return {
        "product_id": product.product_id,
        "name": product.name,
        "unit_price": product.unit_price,
        "tags": sorted(product.tags),
    }


def product_from_dict(data: Dict) -> Product:
    return Product(
        product_id=str(data["product_id"]),
        name=str(data["name"]),
        unit_price=float(data["unit_price"]),
        tags=frozenset(str(tag) for tag in data["tags"]),
    )


def build_default_kind_registry() -> ProductKindRegistry:
    return ProductKindRegistry(
        [
            ProductKind("food", "пищевые товары", frozenset({"food"})),
            ProductKind("chemical", "химия", frozenset({"chemical", "hazard"})),
            ProductKind("electronics", "электроника", frozenset({"electronics"})),
            ProductKind("fragile", "хрупкий товар", frozenset({"fragile"})),
            ProductKind("heavy", "тяжёлый товар", frozenset({"heavy"})),
            ProductKind("household", "бытовые товары", frozenset({"household"})),
        ]
    )
