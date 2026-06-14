from __future__ import annotations

import logging
from decimal import Decimal

import grpc

from cafe_app.exceptions import (
    CategoryAlreadyExistsError,
    CategoryNotFoundError,
    ItemNotFoundError,
)
from cafe_app.grpc_errors import abort_rpc
from cafe_app.grpc_gen import menu_reference_pb2, menu_reference_pb2_grpc
from cafe_app.repositories import InMemoryMenuRepository
from cafe_app.trace_logging import reset_trace_id, set_trace_id
log = logging.getLogger(__name__)


def _trace_id_from_context(context: grpc.ServicerContext) -> str: #Эта функция достаёт Trace ID из gRPC-запроса
    for key, value in context.invocation_metadata(): #gRPC-запрос может иметь metadata. Туда Service A кладёт x-trace-id
        k = key if isinstance(key, str) else key.decode("utf-8", errors="replace")
        if k.lower() == "x-trace-id": #Если среди metadata найден x-trace-id, функция возвращает его
            return value if isinstance(value, str) else value.decode("utf-8", errors="replace")
    return "-" #Если Trace ID не пришёл, используется -


def _item_to_pb(item) -> menu_reference_pb2.ItemMessage: #отправить одно конкретное блюдо. Эта функция переводит обычный объект блюда в protobuf-сообщение
    return menu_reference_pb2.ItemMessage( #Обычный MenuItem превращается в ItemMessage, чтобы его можно было отправить по gRPC обратно в Service A
        id=item.id,
        category_id=item.category_id,
        name=item.name,
        price_decimal_string=str(item.price),
    )


def _entity_to_menu_pb(item) -> menu_reference_pb2.MenuItem: #собрать полное меню целиком.переводит блюдо в protobuf-формат, но в тип MenuItem, который используется внутри полного меню MenuSnapshot
    """В MenuSnapshot поле items имеет тип MenuItem, не ItemMessage."""
    return menu_reference_pb2.MenuItem(
        id=item.id,
        category_id=item.category_id,
        name=item.name,
        price_decimal_string=str(item.price),
    )


class MenuReferenceGrpcServicer(menu_reference_pb2_grpc.MenuReferenceServicer):
    """gRPC-реализация справочника меню (сервис B)."""

    def __init__(self, repo: InMemoryMenuRepository) -> None:
        self._repo = repo #При создании сервиса внутрь передаётся репозиторий меню

    def CreateCategory(self, request, context):  # вызывается, когда Service A хочет создать категорию меню
        token = set_trace_id(_trace_id_from_context(context))
        try:
            log.info("RPC CreateCategory name=%r", request.name)
            try:
                cat = self._repo.add_category(request.name)
            except CategoryAlreadyExistsError as e:
                abort_rpc(context, grpc.StatusCode.ALREADY_EXISTS, str(e), rpc="CreateCategory")
            except CategoryNotFoundError as e:
                abort_rpc(context, grpc.StatusCode.INVALID_ARGUMENT, str(e), rpc="CreateCategory")
            except Exception as e:
                log.exception("RPC CreateCategory → gRPC INTERNAL")
                abort_rpc(context, grpc.StatusCode.INTERNAL, str(e), rpc="CreateCategory")
            return menu_reference_pb2.CategoryMessage(id=cat.id, name=cat.name)
        finally:
            reset_trace_id(token)

    def CreateItem(self, request, context):  # когда Service A хочет создать блюдо
        token = set_trace_id(_trace_id_from_context(context))
        try:
            log.info("RPC CreateItem category_id=%s name=%r", request.category_id, request.name)
            try:
                item = self._repo.add_item(
                    request.category_id,
                    request.name,
                    request.price_text,
                )
            except CategoryNotFoundError as e:
                abort_rpc(context, grpc.StatusCode.NOT_FOUND, str(e), rpc="CreateItem")
            except ItemNotFoundError as e:
                abort_rpc(context, grpc.StatusCode.INVALID_ARGUMENT, str(e), rpc="CreateItem")
            except ValueError as e:
                abort_rpc(context, grpc.StatusCode.INVALID_ARGUMENT, str(e), rpc="CreateItem")
            except Exception as e:
                log.exception("RPC CreateItem → gRPC INTERNAL")
                abort_rpc(context, grpc.StatusCode.INTERNAL, str(e), rpc="CreateItem")
            return _item_to_pb(item)
        finally:
            reset_trace_id(token)

    def GetItem(self, request, context): #Service A хочет получить блюдо по ID  # noqa: N802
        token = set_trace_id(_trace_id_from_context(context))
        try:
            log.info("RPC GetItem item_id=%s", request.item_id)
            try:
                item = self._repo.get_item(request.item_id)
            except ItemNotFoundError as e:
                abort_rpc(context, grpc.StatusCode.NOT_FOUND, str(e), rpc="GetItem")
            except Exception as e:
                log.exception("RPC GetItem → gRPC INTERNAL")
                abort_rpc(context, grpc.StatusCode.INTERNAL, str(e), rpc="GetItem")
            return _item_to_pb(item)
        finally:
            reset_trace_id(token)

    def ListMenu(self, request, context):  #Service A хочет получить всё меню
        token = set_trace_id(_trace_id_from_context(context))
        try:
            log.info("RPC ListMenu")
            try:
                cats = self._repo.list_categories()
                items = list(self._repo.list_items())
                return menu_reference_pb2.MenuSnapshot(
                    categories=[menu_reference_pb2.MenuCategory(id=c.id, name=c.name) for c in cats],
                    items=[_entity_to_menu_pb(it) for it in items],
                )
            except Exception as e:
                log.exception("RPC ListMenu → gRPC INTERNAL")
                abort_rpc(context, grpc.StatusCode.INTERNAL, str(e), rpc="ListMenu")
        finally:
            reset_trace_id(token)
