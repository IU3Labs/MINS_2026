import uuid
from datetime import datetime
from core_service.models import Order

class OrderFactory:
    @staticmethod
    def create_order(customer_name: str, pickup_address: str, delivery_address: str,
                     weight_kg: float, distance_km: float, zone: str, total_cost: float) -> Order:
        order_id = str(uuid.uuid4())[:8]
        return Order(
            order_id=order_id,
            customer_name=customer_name,
            pickup_address=pickup_address,
            delivery_address=delivery_address,
            weight_kg=weight_kg,
            distance_km=distance_km,
            zone=zone,
            total_cost=total_cost,
            status="new",
            created_at=datetime.now().isoformat()
        )