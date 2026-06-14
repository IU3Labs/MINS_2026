from abc import ABC, abstractmethod
from typing import Iterable, Tuple

from lab4_microservices.common.exceptions import CompatibilityError, UserInputError
from lab4_microservices.reference_service.domain.products import Product


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


class ICompatibilityRuleFactory(ABC):
    @abstractmethod
    def create_rules(self) -> list[ICompatibilityRule]:
        raise NotImplementedError

    @abstractmethod
    def factory_name(self) -> str:
        raise NotImplementedError


class FoodChemicalRule(ICompatibilityRule):
    def is_compatible(self, first: Product, second: Product) -> bool:
        return not (
            ("food" in first.tags and "chemical" in second.tags)
            or ("chemical" in first.tags and "food" in second.tags)
        )

    def description(self) -> str:
        return "Пищевые товары нельзя хранить вместе с химией"


class FragileHeavyRule(ICompatibilityRule):
    def is_compatible(self, first: Product, second: Product) -> bool:
        return not (
            ("fragile" in first.tags and "heavy" in second.tags)
            or ("heavy" in first.tags and "fragile" in second.tags)
        )

    def description(self) -> str:
        return "Хрупкие товары нельзя хранить рядом с тяжёлыми"


class HazardElectronicsRule(ICompatibilityRule):
    def is_compatible(self, first: Product, second: Product) -> bool:
        return not (
            ("hazard" in first.tags and "electronics" in second.tags)
            or ("electronics" in first.tags and "hazard" in second.tags)
        )

    def description(self) -> str:
        return "Опасные вещества нельзя хранить рядом с электроникой"


class HouseholdFoodRule(ICompatibilityRule):
    def is_compatible(self, first: Product, second: Product) -> bool:
        return not (
            ("household" in first.tags and "food" in second.tags)
            or ("food" in first.tags and "household" in second.tags)
        )

    def description(self) -> str:
        return "Бытовые товары по внутреннему регламенту нельзя хранить рядом с пищевыми"


class CompatibilityPolicy(ICompatibilityPolicy):
    def __init__(self, rules: Iterable[ICompatibilityRule]):
        self._rules: Tuple[ICompatibilityRule, ...] = tuple(rules)
        if not self._rules:
            raise UserInputError("Политика совместимости должна содержать хотя бы одно правило")

    def ensure_zone_compatible(self, candidate: Product, products_in_zone: Iterable[Product]) -> None:
        for existing in products_in_zone:
            for rule in self._rules:
                if not rule.is_compatible(candidate, existing):
                    raise CompatibilityError(
                        f"Нарушено товарное соседство: '{candidate.name}' и "
                        f"'{existing.name}'. Правило: {rule.description()}"
                    )


class SanPinRuleFactory(ICompatibilityRuleFactory):
    def create_rules(self) -> list[ICompatibilityRule]:
        return [FoodChemicalRule(), HazardElectronicsRule()]

    def factory_name(self) -> str:
        return "sanpin"


class GostRuleFactory(ICompatibilityRuleFactory):
    def create_rules(self) -> list[ICompatibilityRule]:
        return [FragileHeavyRule(), HazardElectronicsRule()]

    def factory_name(self) -> str:
        return "gost"


class InternalWarehouseRuleFactory(ICompatibilityRuleFactory):
    def create_rules(self) -> list[ICompatibilityRule]:
        return [FoodChemicalRule(), FragileHeavyRule(), HazardElectronicsRule(), HouseholdFoodRule()]

    def factory_name(self) -> str:
        return "internal"


class CompatibilityPolicyFactory:
    def __init__(self, rule_factory: ICompatibilityRuleFactory):
        self._rule_factory = rule_factory

    def create_policy(self) -> ICompatibilityPolicy:
        return CompatibilityPolicy(self._rule_factory.create_rules())


class CompatibilityRuleFactoryRegistry:
    def __init__(self, factories: Iterable[ICompatibilityRuleFactory]):
        prepared = {factory.factory_name().lower(): factory for factory in factories}
        if not prepared:
            raise UserInputError("Реестр фабрик правил не может быть пустым")
        self._factories = prepared

    def available_codes(self) -> list[str]:
        return sorted(self._factories)

    def get(self, code: str) -> ICompatibilityRuleFactory:
        normalized_code = code.strip().lower()
        factory = self._factories.get(normalized_code)
        if factory is None:
            available = ", ".join(self.available_codes())
            raise UserInputError(f"Неизвестный набор правил '{code}'. Доступно: {available}")
        return factory


def build_default_rule_factory_registry() -> CompatibilityRuleFactoryRegistry:
    return CompatibilityRuleFactoryRegistry([SanPinRuleFactory(), GostRuleFactory(), InternalWarehouseRuleFactory()])


def build_default_compatibility_policy(rule_set: str = "sanpin") -> ICompatibilityPolicy:
    registry = build_default_rule_factory_registry()
    rule_factory = registry.get(rule_set)
    return CompatibilityPolicyFactory(rule_factory).create_policy()
