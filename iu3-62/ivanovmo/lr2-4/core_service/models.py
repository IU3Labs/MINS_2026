from dataclasses import dataclass
from typing import Optional
from datetime import datetime

@dataclass
class Order:
    order_id: str
    customer_name: str
    pickup_address: str
    delivery_address: str
    weight_kg: float
    distance_km: float
    zone: str
    total_cost: float
    status: str          # "new", "assigned", "in_transit", "delivered"
    assigned_vehicle: Optional[str] = None
    created_at: str = None