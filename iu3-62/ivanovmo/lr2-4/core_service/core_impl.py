import grpc
import logging
import sys
import os
from contextvars import ContextVar

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

import core_pb2
import core_pb2_grpc
import reference_pb2
import reference_pb2_grpc

from core_service.repository import OrderRepository
from core_service.order_factory import OrderFactory
from core_service.tariff_strategy import DEFAULT_STRATEGY
from core_service.order_state import STATE_MAP

logger = logging.getLogger(__name__)
trace_id_var: ContextVar[str] = ContextVar('trace_id', default='no-trace')


class CoreServicer(core_pb2_grpc.CoreServiceServicer):
    def __init__(self, ref_stub, repository: OrderRepository):
        self.ref_stub = ref_stub
        self.repo = repository

    # Вспомогательный метод для безопасного вызова Reference Service
    def _call_reference_safe(self, rpc_method, request, metadata, error_message="Reference service unavailable"):
        try:
            return rpc_method(request, metadata=metadata)
        except grpc.RpcError as e:
            logger.error(f"Reference service RPC failed: {e.code()} - {e.details()}")
            raise Exception(error_message)
        except Exception as e:
            logger.error(f"Unexpected error calling reference service: {e}")
            raise Exception(error_message)

    def CreateOrder(self, request, context):
        trace_id = trace_id_var.get()
        logger.info(f"CreateOrder called for {request.customer_name}")

        try:
            # Валидация адресов через Reference Service
            self._call_reference_safe(
                self.ref_stub.ValidateAddress,
                reference_pb2.AddressRequest(address=request.pickup_address),
                context.invocation_metadata(),
                "Reference service is not available. Please try again later."
            )
            self._call_reference_safe(
                self.ref_stub.ValidateAddress,
                reference_pb2.AddressRequest(address=request.delivery_address),
                context.invocation_metadata(),
                "Reference service is not available. Please try again later."
            )

            # Получение тарифа
            tariff_resp = self._call_reference_safe(
                self.ref_stub.GetTariff,
                reference_pb2.TariffRequest(
                    zone=request.zone,
                    weight_kg=request.weight_kg,
                    distance_km=request.distance_km
                ),
                context.invocation_metadata(),
                "Reference service is not available. Please try again later."
            )

        except Exception as e:
            # Любая ошибка связи – возвращаем понятное сообщение пользователю
            return core_pb2.OrderResponse(
                order_id="",
                status="error",
                total_cost=0.0,
                message=str(e)
            )

        # Расчёт стоимости через стратегию
        cost = DEFAULT_STRATEGY.calculate(
            request.distance_km, request.weight_kg,
            tariff_resp.cost_per_km, tariff_resp.cost_per_kg, tariff_resp.base_cost
        )

        # Создание заказа через фабрику
        order = OrderFactory.create_order(
            request.customer_name, request.pickup_address, request.delivery_address,
            request.weight_kg, request.distance_km, request.zone, cost
        )
        self.repo.add(order)
        logger.info(f"Order {order.order_id} created with cost {cost}")
        return core_pb2.OrderResponse(
            order_id=order.order_id,
            status=order.status,
            total_cost=cost,
            message="Order created successfully"
        )

    # Остальные методы не обращаются к Reference Service – они работают с репозиторием
    def GetOrderStatus(self, request, context):
        order = self.repo.get(request.order_id)
        if not order:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return core_pb2.OrderStatusResponse()
        return core_pb2.OrderStatusResponse(
            order_id=order.order_id,
            status=order.status,
            assigned_vehicle=order.assigned_vehicle or "none",
            last_update=order.created_at
        )

    def AssignTransport(self, request, context):
        order = self.repo.get(request.order_id)
        if not order:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return core_pb2.AssignResponse(success=False, message="Order not found")
        state = STATE_MAP.get(order.status, STATE_MAP["new"])
        result = state.assign_transport(order)
        if order.status == "assigned":
            order.assigned_vehicle = request.vehicle_type_id
            self.repo.update(order)
        return core_pb2.AssignResponse(
            success=(order.status == "assigned"),
            message=result,
            vehicle_id=order.assigned_vehicle or ""
        )

    def UpdateDeliveryStatus(self, request, context):
        order = self.repo.get(request.order_id)
        if not order:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return core_pb2.StatusResponse(success=False, message="Order not found")
        state = STATE_MAP.get(order.status, STATE_MAP["new"])
        if request.new_status == "in_transit":
            result = state.start_delivery(order)
        elif request.new_status == "delivered":
            result = state.complete_delivery(order)
        else:
            return core_pb2.StatusResponse(success=False, message="Invalid status transition")
        self.repo.update(order)
        return core_pb2.StatusResponse(success=True, message=result)

    def GetOrderDetails(self, request, context):
        order = self.repo.get(request.order_id)
        if not order:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return core_pb2.OrderDetailsResponse()
        return core_pb2.OrderDetailsResponse(
            order_id=order.order_id,
            customer_name=order.customer_name,
            pickup_address=order.pickup_address,
            delivery_address=order.delivery_address,
            weight_kg=order.weight_kg,
            distance_km=order.distance_km,
            zone=order.zone,
            total_cost=order.total_cost,
            status=order.status
        )