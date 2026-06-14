from __future__ import annotations

import logging

import grpc

from lab4_microservices.common.exceptions import PersistenceError, StockLevelError, UserInputError, ZoneNotFoundError
from lab4_microservices.core_service.reference_client import (
    ReferenceClient,
    ReferenceUnavailableError,
    ReferenceValidationError,
)
from lab4_microservices.core_service.repository import CoreState, JsonCoreRepository
from lab4_microservices.generated import core_service_pb2
from lab4_microservices.generated import core_service_pb2_grpc


def _set_grpc_error(context, code: grpc.StatusCode, message: str) -> None:
    context.set_code(code)
    context.set_details(message)


class CoreServiceServicer(core_service_pb2_grpc.CoreServiceServicer):
    def __init__(self, repository: JsonCoreRepository, reference_client: ReferenceClient, logger: logging.Logger, rule_set: str):
        self._repo = repository
        self._reference_client = reference_client
        self._logger = logger
        self._rule_set = rule_set

    def AddStock(self, request, context):
        self._logger.info("Запрос AddStock: zone=%s product=%s qty=%s", request.zone, request.product_id, request.qty)
        try:
            state, zone = self._load_and_zone(request.zone)
            self._ensure_positive_qty(request.qty, "добавления")
            self._reference_client.get_product(request.product_id)
            self._reference_client.check_zone_compatibility(
                candidate_product_id=request.product_id,
                existing_product_ids=list(state.zones[zone].keys()),
                rule_set=self._rule_set,
            )
            new_qty = state.zones[zone].get(request.product_id, 0) + request.qty
            state.zones[zone][request.product_id] = new_qty
            self._repo.save(state)
            message = f"В зону {zone} добавлено {request.qty} ед. товара {request.product_id}. Текущий остаток: {new_qty}"
            self._logger.info(message)
            return core_service_pb2.OperationResponse(success=True, message=message)
        except UserInputError as exc:
            return self._operation_error(context, grpc.StatusCode.INVALID_ARGUMENT, exc, "Ошибка AddStock")
        except ZoneNotFoundError as exc:
            return self._operation_error(context, grpc.StatusCode.NOT_FOUND, exc, "Ошибка AddStock")
        except StockLevelError as exc:
            return self._operation_error(context, grpc.StatusCode.FAILED_PRECONDITION, exc, "Ошибка AddStock")
        except ReferenceValidationError as exc:
            return self._operation_error(context, exc.status_code, exc, "Ошибка AddStock")
        except ReferenceUnavailableError as exc:
            return self._operation_error(context, grpc.StatusCode.UNAVAILABLE, exc, "Reference Service недоступен при AddStock", error=True)
        except PersistenceError as exc:
            return self._operation_error(context, grpc.StatusCode.INTERNAL, exc, "Ошибка хранения данных при AddStock", error=True)

    def RemoveStock(self, request, context):
        self._logger.info("Запрос RemoveStock: zone=%s product=%s qty=%s", request.zone, request.product_id, request.qty)
        try:
            state, zone = self._load_and_zone(request.zone)
            self._ensure_positive_qty(request.qty, "списания")
            current_qty = state.zones[zone].get(request.product_id, 0)
            if current_qty < request.qty:
                raise StockLevelError(f"Недостаточно товара в зоне {zone}. Есть {current_qty}, нужно {request.qty}")
            new_qty = current_qty - request.qty
            self._write_updated_qty(state, zone, request.product_id, new_qty)
            self._repo.save(state)
            message = f"Из зоны {zone} списано {request.qty} ед. товара {request.product_id}. Текущий остаток: {new_qty}"
            self._logger.info(message)
            return core_service_pb2.OperationResponse(success=True, message=message)
        except UserInputError as exc:
            return self._operation_error(context, grpc.StatusCode.INVALID_ARGUMENT, exc, "Ошибка RemoveStock")
        except ZoneNotFoundError as exc:
            return self._operation_error(context, grpc.StatusCode.NOT_FOUND, exc, "Ошибка RemoveStock")
        except StockLevelError as exc:
            return self._operation_error(context, grpc.StatusCode.FAILED_PRECONDITION, exc, "Ошибка RemoveStock")
        except PersistenceError as exc:
            return self._operation_error(context, grpc.StatusCode.INTERNAL, exc, "Ошибка хранения данных при RemoveStock", error=True)

    def MoveStock(self, request, context):
        self._logger.info(
            "Запрос MoveStock: from=%s to=%s product=%s qty=%s",
            request.from_zone,
            request.to_zone,
            request.product_id,
            request.qty,
        )
        try:
            self._ensure_positive_qty(request.qty, "перемещения")
            state = self._repo.load()
            source_zone = self._normalize_zone(state, request.from_zone)
            target_zone = self._normalize_zone(state, request.to_zone)
            if source_zone == target_zone:
                raise UserInputError("Исходная и целевая зоны должны различаться")
            current_qty = state.zones[source_zone].get(request.product_id, 0)
            if current_qty < request.qty:
                raise StockLevelError(
                    f"Недостаточно товара в зоне {source_zone}. Есть {current_qty}, нужно {request.qty}"
                )
            self._reference_client.get_product(request.product_id)
            self._reference_client.check_zone_compatibility(
                candidate_product_id=request.product_id,
                existing_product_ids=list(state.zones[target_zone].keys()),
                rule_set=self._rule_set,
            )
            source_new_qty = current_qty - request.qty
            target_new_qty = state.zones[target_zone].get(request.product_id, 0) + request.qty
            self._write_updated_qty(state, source_zone, request.product_id, source_new_qty)
            state.zones[target_zone][request.product_id] = target_new_qty
            self._repo.save(state)
            message = (
                f"Товар {request.product_id} перемещён: {request.qty} ед. "
                f"из зоны {source_zone} в зону {target_zone}."
            )
            self._logger.info(message)
            return core_service_pb2.OperationResponse(success=True, message=message)
        except UserInputError as exc:
            return self._operation_error(context, grpc.StatusCode.INVALID_ARGUMENT, exc, "Ошибка MoveStock")
        except ZoneNotFoundError as exc:
            return self._operation_error(context, grpc.StatusCode.NOT_FOUND, exc, "Ошибка MoveStock")
        except StockLevelError as exc:
            return self._operation_error(context, grpc.StatusCode.FAILED_PRECONDITION, exc, "Ошибка MoveStock")
        except ReferenceValidationError as exc:
            return self._operation_error(context, exc.status_code, exc, "Ошибка MoveStock")
        except ReferenceUnavailableError as exc:
            return self._operation_error(context, grpc.StatusCode.UNAVAILABLE, exc, "Reference Service недоступен при MoveStock", error=True)
        except PersistenceError as exc:
            return self._operation_error(context, grpc.StatusCode.INTERNAL, exc, "Ошибка хранения данных при MoveStock", error=True)

    def ListZones(self, request, context):
        self._logger.info("Запрос ListZones")
        try:
            zones = sorted(self._repo.load().zones)
            return core_service_pb2.ListZonesResponse(zones=zones)
        except PersistenceError as exc:
            message = str(exc)
            self._logger.error("Ошибка хранения данных при ListZones: %s", message)
            _set_grpc_error(context, grpc.StatusCode.INTERNAL, message)
            return core_service_pb2.ListZonesResponse(zones=[])

    def ListZone(self, request, context):
        self._logger.info("Запрос ListZone для зоны %s", request.zone)
        try:
            state, zone = self._load_and_zone(request.zone)
            items = []
            for product_id, qty in sorted(state.zones[zone].items()):
                product = self._reference_client.get_product(product_id)
                items.append(core_service_pb2.ZoneItem(product=product, qty=qty))
            return core_service_pb2.ListZoneResponse(success=True, message="OK", items=items)
        except ZoneNotFoundError as exc:
            return self._list_zone_error(context, grpc.StatusCode.NOT_FOUND, exc, "Ошибка ListZone")
        except ReferenceValidationError as exc:
            return self._list_zone_error(context, exc.status_code, exc, "Ошибка ListZone")
        except ReferenceUnavailableError as exc:
            return self._list_zone_error(context, grpc.StatusCode.UNAVAILABLE, exc, "Reference Service недоступен при ListZone", error=True)
        except PersistenceError as exc:
            return self._list_zone_error(context, grpc.StatusCode.INTERNAL, exc, "Ошибка хранения данных при ListZone", error=True)

    def GetSummary(self, request, context):
        self._logger.info("Запрос GetSummary")
        try:
            state = self._repo.load()
            products = {product.product_id: product for product in self._reference_client.list_products(sort_by="id")}

            total_units = 0
            total_positions = 0
            total_value = 0.0
            by_zone = []
            by_tag: dict[str, int] = {}
            product_units: dict[str, int] = {}
            product_values: dict[str, float] = {}

            for zone_name, items in sorted(state.zones.items()):
                zone_units = sum(items.values())
                zone_positions = len(items)
                zone_value = 0.0
                for product_id, qty in items.items():
                    product = products.get(product_id)
                    if product is None:
                        raise ReferenceValidationError(
                            f"Товар с ID={product_id} отсутствует в справочном каталоге",
                            grpc.StatusCode.NOT_FOUND,
                        )
                    item_value = product.unit_price * qty
                    zone_value += item_value
                    total_value += item_value
                    product_units[product_id] = product_units.get(product_id, 0) + qty
                    product_values[product_id] = product_values.get(product_id, 0.0) + item_value
                    for tag in product.tags:
                        by_tag[tag] = by_tag.get(tag, 0) + qty
                total_units += zone_units
                total_positions += zone_positions
                by_zone.append(
                    core_service_pb2.ZoneStats(
                        zone=zone_name,
                        units=zone_units,
                        positions=zone_positions,
                        value=round(zone_value, 2),
                    )
                )

            by_product = []
            for product_id in sorted(product_units):
                product = products[product_id]
                by_product.append(
                    core_service_pb2.ProductStats(
                        product_id=product_id,
                        name=product.name,
                        units=product_units[product_id],
                        value=round(product_values[product_id], 2),
                    )
                )

            summary = core_service_pb2.WarehouseSummary(
                total_units=total_units,
                total_positions=total_positions,
                total_value=round(total_value, 2),
                by_zone=by_zone,
                by_tag=[core_service_pb2.TagStats(tag=tag, units=units) for tag, units in sorted(by_tag.items())],
                by_product=by_product,
            )
            return core_service_pb2.GetSummaryResponse(success=True, message="OK", summary=summary)
        except ReferenceValidationError as exc:
            return self._summary_error(context, exc.status_code, exc, "Ошибка GetSummary")
        except ReferenceUnavailableError as exc:
            return self._summary_error(context, grpc.StatusCode.UNAVAILABLE, exc, "Reference Service недоступен при GetSummary", error=True)
        except PersistenceError as exc:
            return self._summary_error(context, grpc.StatusCode.INTERNAL, exc, "Ошибка хранения данных при GetSummary", error=True)

    def _operation_error(self, context, code: grpc.StatusCode, exc: Exception, log_prefix: str, error: bool = False):
        message = str(exc)
        if error:
            self._logger.error("%s: %s", log_prefix, message)
        else:
            self._logger.warning("%s: %s", log_prefix, message)
        _set_grpc_error(context, code, message)
        return core_service_pb2.OperationResponse(success=False, message=message)

    def _list_zone_error(self, context, code: grpc.StatusCode, exc: Exception, log_prefix: str, error: bool = False):
        message = str(exc)
        if error:
            self._logger.error("%s: %s", log_prefix, message)
        else:
            self._logger.warning("%s: %s", log_prefix, message)
        _set_grpc_error(context, code, message)
        return core_service_pb2.ListZoneResponse(success=False, message=message)

    def _summary_error(self, context, code: grpc.StatusCode, exc: Exception, log_prefix: str, error: bool = False):
        message = str(exc)
        if error:
            self._logger.error("%s: %s", log_prefix, message)
        else:
            self._logger.warning("%s: %s", log_prefix, message)
        _set_grpc_error(context, code, message)
        return core_service_pb2.GetSummaryResponse(success=False, message=message)

    def _load_and_zone(self, zone: str):
        state = self._repo.load()
        return state, self._normalize_zone(state, zone)

    @staticmethod
    def _normalize_zone(state: CoreState, zone: str) -> str:
        normalized_zone = zone.strip().upper()
        if normalized_zone not in state.zones:
            available = ", ".join(sorted(state.zones))
            raise ZoneNotFoundError(f"Зона '{normalized_zone}' не существует. Доступно: {available}")
        return normalized_zone

    @staticmethod
    def _ensure_positive_qty(qty: int, action_name: str) -> None:
        if qty <= 0:
            raise UserInputError(f"Количество для {action_name} должно быть больше 0")

    @staticmethod
    def _write_updated_qty(state: CoreState, zone: str, product_id: str, new_qty: int) -> None:
        if new_qty <= 0:
            state.zones[zone].pop(product_id, None)
        else:
            state.zones[zone][product_id] = new_qty
