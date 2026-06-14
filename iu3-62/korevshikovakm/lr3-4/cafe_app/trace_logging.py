from __future__ import annotations

import contextvars
import logging
import sys
from collections.abc import Iterator
from contextlib import contextmanager

TRACE_ID_CTX: contextvars.ContextVar[str] = contextvars.ContextVar("trace_id", default="-") #Здесь хранится текущий трейс айди


class TraceIdFilter(logging.Filter): #Лобавляете трейс айди в каждую запись лога
    def filter(self, record: logging.LogRecord) -> bool:
        record.trace_id = TRACE_ID_CTX.get() #Берем текущий трес айди из TRACE_ID_CTX и добавляем в лог запись
        return True


def setup_logging(service_label: str) -> None: #Настраивает формат логов ждя сервиса
    handler = logging.StreamHandler(sys.stdout) #Обработчик логов, который выводит сообщения в консоль
    handler.setFormatter(
        logging.Formatter(f"%(levelname)s [{service_label}] [%(trace_id)s] %(message)s") #формат логов
    )
    handler.addFilter(TraceIdFilter()) #К обработчику добавляется TraceIdFilter (он подставляет trace_id в формат логов)
    root = logging.getLogger() #берется корневой логгер
    root.handlers.clear() #очищаются старые обработчики
    root.addHandler(handler) #Добавляется новый обработчик
    root.setLevel(logging.INFO) #Уровень логированя становится инфо


def set_trace_id(trace_id: str) -> contextvars.Token[str]:
    return TRACE_ID_CTX.set(trace_id) #Устанавливает текущий трейс айди, токен нужен чтобы потом вернуть предыдущее значение трейс айди


def reset_trace_id(token: contextvars.Token[str]) -> None:
    TRACE_ID_CTX.reset(token) #откатывает трейс айди назад, чтобв трейс айди одного запроса не попал в следующий


@contextmanager
def trace_scope(trace_id: str) -> Iterator[None]:
    token = set_trace_id(trace_id)
    try:
        yield
    finally:
        reset_trace_id(token)
