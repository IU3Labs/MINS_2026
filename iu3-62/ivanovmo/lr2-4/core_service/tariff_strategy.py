from abc import ABC, abstractmethod

class TariffStrategy(ABC):
    @abstractmethod
    def calculate(self, distance_km: float, weight_kg: float, cost_per_km: float, cost_per_kg: float, base_cost: float) -> float:
        pass

class SimpleDistanceWeightStrategy(TariffStrategy):
    def calculate(self, distance_km: float, weight_kg: float, cost_per_km: float, cost_per_kg: float, base_cost: float) -> float:
        return base_cost + distance_km * cost_per_km + weight_kg * cost_per_kg

class ExpressStrategy(TariffStrategy):
    def calculate(self, distance_km: float, weight_kg: float, cost_per_km: float, cost_per_kg: float, base_cost: float) -> float:
        return base_cost * 1.5 + distance_km * cost_per_km * 1.2 + weight_kg * cost_per_kg

# Default strategy
DEFAULT_STRATEGY = SimpleDistanceWeightStrategy()