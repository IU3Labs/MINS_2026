import threading
from typing import Dict, Optional
from core_service.models import Order

class OrderRepository:
    def __init__(self):
        self._orders: Dict[str, Order] = {}
        self._lock = threading.RLock()

    def add(self, order: Order):
        with self._lock:
            self._orders[order.order_id] = order

    def get(self, order_id: str) -> Optional[Order]:
        with self._lock:
            return self._orders.get(order_id)

    def update(self, order: Order):
        with self._lock:
            self._orders[order.order_id] = order

    def get_all(self):
        with self._lock:
            return list(self._orders.values())