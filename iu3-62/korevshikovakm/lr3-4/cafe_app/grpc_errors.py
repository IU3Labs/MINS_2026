from __future__ import annotations

import logging
from typing import NoReturn

import grpc

from cafe_app.exceptions import ReferenceServiceUnavailableError
from cafe_app.trace_logging import TRACE_ID_CTX

log = logging.getLogger(__name__)

_UNAVAILABLE_CODES = frozenset(
    {
        grpc.StatusCode.UNAVAILABLE,
        grpc.StatusCode.DEADLINE_EXCEEDED,
        grpc.StatusCode.UNKNOWN,
        grpc.StatusCode.CANCELLED,
        grpc.StatusCode.UNIMPLEMENTED,
    }
)


def grpc_status_name(exc: grpc.RpcError) -> str:
    code = exc.code()
    if hasattr(code, "name"):
        return code.name
    return str(code)


def is_reference_service_unreachable(exc: grpc.RpcError) -> bool: #Сервис B выключен, порт закрыт, таймаут и т.п

    if exc.code() in _UNAVAILABLE_CODES:
        return True
    details = (exc.details() or "").lower()
    markers = (
        "failed to connect",
        "connection refused",
        "connection reset",
        "name resolution",
        "no connection established",
        "socket closed",
        "unavailable",
    )
    return any(m in details for m in markers)


def format_local_error_message(exc: BaseException) -> str: #оформляет обычную ошибку внутри Service A,
    parts: list[str] = ["Ошибка:"]
    trace_id = TRACE_ID_CTX.get()
    if trace_id and trace_id != "-":
        parts.append(f"[trace_id={trace_id}]")
    parts.append(str(exc))
    return " ".join(parts)


def format_user_error_message(exc: BaseException) -> str: #оформляет ошибку для вывода пользователю
    parts: list[str] = ["Ошибка:"]
    grpc_status = getattr(exc, "grpc_status", None)
    if not grpc_status and isinstance(exc, ReferenceServiceUnavailableError):
        grpc_status = "UNAVAILABLE"
    if grpc_status:
        parts.append(f"[gRPC {grpc_status}]")
    trace_id = getattr(exc, "trace_id", None) or TRACE_ID_CTX.get()
    if trace_id and trace_id != "-":
        parts.append(f"[trace_id={trace_id}]")
    parts.append(str(exc))
    return " ".join(parts)


def abort_rpc(context: grpc.ServicerContext,code: grpc.StatusCode,message: str,*,rpc: str,) -> NoReturn: #Логирует ошибку и отправляет Service A gRPC-статус
    log.error("RPC %s → gRPC %s: %s", rpc, code.name, message)
    context.abort(code, message)


def log_rpc_failure_on_client(exc: grpc.RpcError,*,rpc: str,) -> None: #Пишет в лог, какой gRPC-вызов упал
    try:
        details = exc.details() or "(без описания)"
    except Exception:
        details = "(не удалось прочитать описание)"
    log.error(
        "gRPC %s ← ответ %s: %s",
        rpc,
        grpc_status_name(exc),
        details,
    )

