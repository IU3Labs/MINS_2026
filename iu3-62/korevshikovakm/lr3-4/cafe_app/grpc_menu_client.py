from __future__ import annotations

import logging
from decimal import Decimal
from typing import NoReturn

import grpc

from cafe_app.entities import MenuCategory, MenuItem
from cafe_app.exceptions import (
    CategoryAlreadyExistsError,
    CategoryNotFoundError,
    ItemNotFoundError,
    ReferenceServiceUnavailableError,
    ValidationError,
)
from cafe_app.grpc_errors import (
    grpc_status_name,
    is_reference_service_unreachable,
    log_rpc_failure_on_client,
)
from cafe_app.grpc_gen import menu_reference_pb2, menu_reference_pb2_grpc
from cafe_app.trace_logging import TRACE_ID_CTX

log = logging.getLogger(__name__)


class GrpcMenuRepository:
    """Доступ к меню только через gRPC (сервис B)."""

    def __init__(self, channel: grpc.Channel, timeout: float = 5.0) -> None:
        self._stub = menu_reference_pb2_grpc.MenuReferenceStub(channel)
        self._timeout = timeout

    def _metadata(self) -> tuple[tuple[str, str], ...]:
        return (("x-trace-id", TRACE_ID_CTX.get()),)

    def _trace_id(self) -> str:
        return TRACE_ID_CTX.get()

    def _handle_rpc_error(self, exc: grpc.RpcError, *, rpc: str) -> NoReturn:
        log_rpc_failure_on_client(exc, rpc=rpc)
        status = grpc_status_name(exc)
        trace_id = self._trace_id()
        try:
            details = exc.details() or ""
        except Exception:
            details = ""

        if is_reference_service_unreachable(exc):
            raise ReferenceServiceUnavailableError(
                "Справочник меню (сервис B) временно недоступен. "
                "Запустите сервер справочника (run_reference_server.py) и проверьте адрес подключения.",
                grpc_status=status,
                trace_id=trace_id,
            ) from exc
        if exc.code() == grpc.StatusCode.ALREADY_EXISTS:
            raise CategoryAlreadyExistsError(
                details or "Категория уже существует.",
                grpc_status=status,
                trace_id=trace_id,
            ) from exc
        if exc.code() == grpc.StatusCode.NOT_FOUND:
            if rpc == "GetItem":
                raise ItemNotFoundError(
                    details or "Блюдо не найдено.",
                    grpc_status=status,
                    trace_id=trace_id,
                ) from exc
            raise CategoryNotFoundError(
                details or "Категория не найдена.",
                grpc_status=status,
                trace_id=trace_id,
            ) from exc
        if exc.code() == grpc.StatusCode.INVALID_ARGUMENT:
            if rpc == "CreateItem":
                raise ValidationError(
                    details or "Некорректные данные блюда.",
                    grpc_status=status,
                    trace_id=trace_id,
                ) from exc
            raise CategoryNotFoundError(
                details or "Ошибка категории.",
                grpc_status=status,
                trace_id=trace_id,
            ) from exc
        raise ValidationError(
            details or f"gRPC: {status}",
            grpc_status=status,
            trace_id=trace_id,
        ) from exc

    def add_category(self, name: str) -> MenuCategory:
        try:
            log.info("gRPC CreateCategory")
            r = self._stub.CreateCategory(
                menu_reference_pb2.CreateCategoryRequest(name=name),
                metadata=self._metadata(),
                timeout=self._timeout,
            )
            return MenuCategory(id=r.id, name=r.name)
        except grpc.RpcError as e:
            self._handle_rpc_error(e, rpc="CreateCategory")

    def add_item(self, category_id: int, name: str, price_text: str) -> MenuItem:
        try:
            log.info("gRPC CreateItem category_id=%s", category_id)
            r = self._stub.CreateItem(
                menu_reference_pb2.CreateItemRequest(
                    category_id=category_id,
                    name=name,
                    price_text=price_text,
                ),
                metadata=self._metadata(),
                timeout=self._timeout,
            )
            return MenuItem(
                id=r.id,
                category_id=r.category_id,
                name=r.name,
                price=Decimal(r.price_decimal_string),
            )
        except grpc.RpcError as e:
            self._handle_rpc_error(e, rpc="CreateItem")

    def list_categories(self) -> list[MenuCategory]:
        snap = self._list_menu_snapshot()
        return [MenuCategory(id=c.id, name=c.name) for c in snap.categories]

    def list_items(self, category_id: int | None = None) -> list[MenuItem]:
        snap = self._list_menu_snapshot()
        items = [
            MenuItem(
                id=it.id,
                category_id=it.category_id,
                name=it.name,
                price=Decimal(it.price_decimal_string),
            )
            for it in snap.items
        ]
        if category_id is None:
            return sorted(items, key=lambda x: x.id)
        return sorted([it for it in items if it.category_id == category_id], key=lambda x: x.id)

    def get_item(self, item_id: int) -> MenuItem:
        try:
            log.info("gRPC GetItem item_id=%s", item_id)
            r = self._stub.GetItem(
                menu_reference_pb2.GetItemRequest(item_id=item_id),
                metadata=self._metadata(),
                timeout=self._timeout,
            )
            return MenuItem(
                id=r.id,
                category_id=r.category_id,
                name=r.name,
                price=Decimal(r.price_decimal_string),
            )
        except grpc.RpcError as e:
            self._handle_rpc_error(e, rpc="GetItem")

    def get_category(self, category_id: int) -> MenuCategory:
        for c in self.list_categories():
            if c.id == category_id:
                return c
        raise CategoryNotFoundError(
            "Категория не найдена.",
            trace_id=self._trace_id(),
        )

    def _list_menu_snapshot(self) -> menu_reference_pb2.MenuSnapshot:
        try:
            log.info("gRPC ListMenu")
            return self._stub.ListMenu(
                menu_reference_pb2.Empty(),
                metadata=self._metadata(),
                timeout=self._timeout,
            )
        except grpc.RpcError as e:
            self._handle_rpc_error(e, rpc="ListMenu")
