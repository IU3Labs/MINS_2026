from __future__ import annotations

import grpc

from lab4_microservices.common.grpc_utils import metadata_with_trace
from lab4_microservices.generated import reference_service_pb2
from lab4_microservices.generated import reference_service_pb2_grpc

_BUSINESS_ERROR_CODES = {
    grpc.StatusCode.INVALID_ARGUMENT,
    grpc.StatusCode.NOT_FOUND,
    grpc.StatusCode.ALREADY_EXISTS,
    grpc.StatusCode.FAILED_PRECONDITION,
}


class ReferenceUnavailableError(Exception):
    def __init__(self, message: str, status_code: grpc.StatusCode = grpc.StatusCode.UNAVAILABLE):
        super().__init__(message)
        self.status_code = status_code


class ReferenceValidationError(Exception):
    def __init__(self, message: str, status_code: grpc.StatusCode = grpc.StatusCode.FAILED_PRECONDITION):
        super().__init__(message)
        self.status_code = status_code


class ReferenceClient:
    def __init__(self, target: str):
        self._channel = grpc.insecure_channel(target)
        self._stub = reference_service_pb2_grpc.ReferenceServiceStub(self._channel)

    def get_product(self, product_id: str):
        try:
            response = self._stub.GetProduct(
                reference_service_pb2.GetProductRequest(product_id=product_id),
                metadata=metadata_with_trace(),
                timeout=3,
            )
        except grpc.RpcError as exc:
            self._raise_reference_error(exc)
        if not response.success:
            raise ReferenceValidationError(response.message, grpc.StatusCode.NOT_FOUND)
        return response.product

    def list_products(self, sort_by: str = "id"):
        try:
            response = self._stub.ListProducts(
                reference_service_pb2.ListProductsRequest(sort_by=sort_by),
                metadata=metadata_with_trace(),
                timeout=3,
            )
        except grpc.RpcError as exc:
            self._raise_reference_error(exc)
        if not response.success:
            raise ReferenceValidationError(response.message, grpc.StatusCode.INVALID_ARGUMENT)
        return list(response.products)

    def check_zone_compatibility(self, candidate_product_id: str, existing_product_ids: list[str], rule_set: str):
        try:
            response = self._stub.CheckZoneCompatibility(
                reference_service_pb2.CompatibilityCheckRequest(
                    candidate_product_id=candidate_product_id,
                    existing_product_ids=existing_product_ids,
                    rule_set=rule_set,
                ),
                metadata=metadata_with_trace(),
                timeout=3,
            )
        except grpc.RpcError as exc:
            self._raise_reference_error(exc)
        if not response.success or not response.compatible:
            raise ReferenceValidationError(response.message, grpc.StatusCode.FAILED_PRECONDITION)
        return True

    @staticmethod
    def _raise_reference_error(exc: grpc.RpcError) -> None:
        code = exc.code() if hasattr(exc, "code") else grpc.StatusCode.UNKNOWN
        details = exc.details() if hasattr(exc, "details") else str(exc)
        message = details or str(exc)
        if code in _BUSINESS_ERROR_CODES:
            raise ReferenceValidationError(message, code) from exc
        raise ReferenceUnavailableError(ReferenceClient._build_unavailable_message(exc), code) from exc

    @staticmethod
    def _build_unavailable_message(exc: grpc.RpcError) -> str:
        details = exc.details() if hasattr(exc, "details") else str(exc)
        return (
            "Справочный сервис недоступен. Операция не выполнена. "
            f"Повторите попытку позже. Техническая деталь: {details}"
        )
