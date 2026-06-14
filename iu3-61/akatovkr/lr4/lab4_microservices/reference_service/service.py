from __future__ import annotations

import logging

import grpc

from lab4_microservices.common.exceptions import (
    CompatibilityError,
    DuplicateProductError,
    PersistenceError,
    ProductNotFoundError,
    UserInputError,
)
from lab4_microservices.generated import reference_service_pb2
from lab4_microservices.generated import reference_service_pb2_grpc
from lab4_microservices.reference_service.domain.compatibility import build_default_compatibility_policy
from lab4_microservices.reference_service.domain.products import (
    ProductFactory,
    build_default_kind_registry,
    product_from_dict,
    product_to_dict,
)
from lab4_microservices.reference_service.domain.sorting import build_default_sort_strategy_registry
from lab4_microservices.reference_service.repository import JsonReferenceRepository


def _set_grpc_error(context, code: grpc.StatusCode, message: str) -> None:
    context.set_code(code)
    context.set_details(message)


def _status_for_exception(exc: Exception) -> grpc.StatusCode:
    if isinstance(exc, DuplicateProductError):
        return grpc.StatusCode.ALREADY_EXISTS
    if isinstance(exc, ProductNotFoundError):
        return grpc.StatusCode.NOT_FOUND
    if isinstance(exc, CompatibilityError):
        return grpc.StatusCode.FAILED_PRECONDITION
    if isinstance(exc, UserInputError):
        return grpc.StatusCode.INVALID_ARGUMENT
    if isinstance(exc, PersistenceError):
        return grpc.StatusCode.INTERNAL
    return grpc.StatusCode.UNKNOWN


class ReferenceServiceServicer(reference_service_pb2_grpc.ReferenceServiceServicer):
    def __init__(self, repository: JsonReferenceRepository, logger: logging.Logger):
        self._repo = repository
        self._logger = logger
        self._kind_registry = build_default_kind_registry()
        self._product_factory = ProductFactory(self._kind_registry)
        self._sort_registry = build_default_sort_strategy_registry()

    def AddProduct(self, request, context):
        self._logger.info("Запрос AddProduct для товара %s", request.product_id)
        try:
            product = self._product_factory.create(
                product_id=request.product_id,
                name=request.name,
                unit_price=request.unit_price,
                kind_codes=request.kind_codes,
            )
            state = self._repo.load()
            if product.product_id in state.catalog:
                raise DuplicateProductError(f"Товар с ID={product.product_id} уже существует")
            state.catalog[product.product_id] = product_to_dict(product)
            self._repo.save(state)
            self._logger.info("Товар %s добавлен в справочный каталог", product.product_id)
            return reference_service_pb2.AddProductResponse(
                success=True,
                message="Товар успешно добавлен в каталог справочного сервиса.",
                product=self._to_proto_product(product),
            )
        except (UserInputError, DuplicateProductError, PersistenceError) as exc:
            return self._add_product_error(context, exc, "Ошибка AddProduct")

    def GetProduct(self, request, context):
        self._logger.info("Запрос GetProduct для товара %s", request.product_id)
        try:
            product = self._get_product(request.product_id)
            return reference_service_pb2.GetProductResponse(
                success=True,
                message="OK",
                product=self._to_proto_product(product),
            )
        except (ProductNotFoundError, PersistenceError) as exc:
            return self._get_product_error(context, exc, "Ошибка GetProduct")

    def ListProducts(self, request, context):
        sort_by = request.sort_by or self._sort_registry.default().code
        self._logger.info("Запрос ListProducts со стратегией сортировки %s", sort_by)
        try:
            state = self._repo.load()
            products = [product_from_dict(item) for item in state.catalog.values()]
            strategy = self._sort_registry.get(sort_by)
            sorted_products = strategy.sort(products)
            return reference_service_pb2.ListProductsResponse(
                success=True,
                message="OK",
                products=[self._to_proto_product(product) for product in sorted_products],
            )
        except (UserInputError, PersistenceError) as exc:
            return self._list_products_error(context, exc, "Ошибка ListProducts")

    def ListKinds(self, request, context):
        self._logger.info("Запрос ListKinds")
        kinds = []
        for code in self._kind_registry.available_codes():
            kind = self._kind_registry._kinds[code]
            kinds.append(reference_service_pb2.KindInfo(code=kind.code, title=kind.title))
        return reference_service_pb2.ListKindsResponse(kinds=kinds)

    def CheckZoneCompatibility(self, request, context):
        self._logger.info(
            "Запрос CheckZoneCompatibility для товара %s с набором правил %s",
            request.candidate_product_id,
            request.rule_set,
        )
        try:
            state = self._repo.load()
            candidate = self._get_product(request.candidate_product_id)
            existing_products = []
            for product_id in request.existing_product_ids:
                raw = state.catalog.get(product_id)
                if raw is None:
                    raise ProductNotFoundError(f"Товар с ID={product_id} отсутствует в справочном каталоге")
                existing_products.append(product_from_dict(raw))

            policy = build_default_compatibility_policy(request.rule_set or "sanpin")
            policy.ensure_zone_compatible(candidate, existing_products)
            return reference_service_pb2.CompatibilityCheckResponse(
                success=True,
                message="Проверка совместимости пройдена.",
                compatible=True,
            )
        except (UserInputError, ProductNotFoundError, CompatibilityError, PersistenceError) as exc:
            return self._compatibility_error(context, exc, "Ошибка CheckZoneCompatibility")

    def _add_product_error(self, context, exc: Exception, log_prefix: str):
        return self._make_error_response(
            context,
            exc,
            log_prefix,
            reference_service_pb2.AddProductResponse,
        )

    def _get_product_error(self, context, exc: Exception, log_prefix: str):
        return self._make_error_response(
            context,
            exc,
            log_prefix,
            reference_service_pb2.GetProductResponse,
        )

    def _list_products_error(self, context, exc: Exception, log_prefix: str):
        return self._make_error_response(
            context,
            exc,
            log_prefix,
            reference_service_pb2.ListProductsResponse,
        )

    def _compatibility_error(self, context, exc: Exception, log_prefix: str):
        return self._make_error_response(
            context,
            exc,
            log_prefix,
            reference_service_pb2.CompatibilityCheckResponse,
            compatible=False,
        )

    def _make_error_response(self, context, exc: Exception, log_prefix: str, response_cls, **extra_fields):
        message = str(exc)
        code = _status_for_exception(exc)
        if code == grpc.StatusCode.INTERNAL:
            self._logger.error("%s: %s", log_prefix, message)
        else:
            self._logger.warning("%s: %s", log_prefix, message)
        _set_grpc_error(context, code, message)
        return response_cls(success=False, message=message, **extra_fields)

    def _get_product(self, product_id: str):
        state = self._repo.load()
        raw = state.catalog.get(product_id)
        if raw is None:
            raise ProductNotFoundError(f"Товар с ID={product_id} не найден")
        return product_from_dict(raw)

    @staticmethod
    def _to_proto_product(product):
        return reference_service_pb2.Product(
            product_id=product.product_id,
            name=product.name,
            unit_price=product.unit_price,
            tags=sorted(product.tags),
        )
