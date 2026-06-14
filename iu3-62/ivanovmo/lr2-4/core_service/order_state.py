from abc import ABC, abstractmethod

class OrderState(ABC):
    @abstractmethod
    def assign_transport(self, order) -> str:
        pass

    @abstractmethod
    def start_delivery(self, order) -> str:
        pass

    @abstractmethod
    def complete_delivery(self, order) -> str:
        pass

    @property
    @abstractmethod
    def name(self) -> str:
        pass

class NewState(OrderState):
    def assign_transport(self, order):
        order.status = "assigned"
        return "Transport assigned"
    def start_delivery(self, order):
        return "Cannot start delivery: order not assigned"
    def complete_delivery(self, order):
        return "Cannot complete: order not assigned"
    @property
    def name(self): return "new"

class AssignedState(OrderState):
    def assign_transport(self, order):
        return "Transport already assigned"
    def start_delivery(self, order):
        order.status = "in_transit"
        return "Delivery started"
    def complete_delivery(self, order):
        return "Cannot complete: delivery not started"
    @property
    def name(self): return "assigned"

class InTransitState(OrderState):
    def assign_transport(self, order):
        return "Cannot assign: already in transit"
    def start_delivery(self, order):
        return "Already in transit"
    def complete_delivery(self, order):
        order.status = "delivered"
        return "Delivery completed"
    @property
    def name(self): return "in_transit"

class DeliveredState(OrderState):
    def assign_transport(self, order):
        return "Already delivered"
    def start_delivery(self, order):
        return "Already delivered"
    def complete_delivery(self, order):
        return "Already delivered"
    @property
    def name(self): return "delivered"

STATE_MAP = {
    "new": NewState(),
    "assigned": AssignedState(),
    "in_transit": InTransitState(),
    "delivered": DeliveredState()
}