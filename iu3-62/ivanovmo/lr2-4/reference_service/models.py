from dataclasses import dataclass
from typing import Dict, List

@dataclass
class Tariff:
    zone: str
    cost_per_km: float
    cost_per_kg: float
    base_cost: float

@dataclass
class VehicleType:
    id: str
    name: str
    max_weight_kg: float

# In-memory data
TARIFFS: Dict[str, Tariff] = {
    "city": Tariff("city", 2.5, 3.0, 50.0),
    "suburb": Tariff("suburb", 3.0, 3.5, 70.0),
    "rural": Tariff("rural", 4.0, 4.0, 100.0),
}

VEHICLE_TYPES: List[VehicleType] = [
    VehicleType("van_small", "Small Van", 800.0),
    VehicleType("van_large", "Large Van", 1500.0),
    VehicleType("truck", "Truck", 5000.0),
]

VALID_ADDRESSES = {"moscow", "spb", "kazan", "novosibirsk"}  # simplified demo