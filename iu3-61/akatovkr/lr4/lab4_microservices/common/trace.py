from __future__ import annotations

import contextvars
import logging
import uuid
from pathlib import Path

TRACE_ID_HEADER = "x-trace-id"
_trace_id_var: contextvars.ContextVar[str] = contextvars.ContextVar("trace_id", default="-")


def get_trace_id() -> str:
    return _trace_id_var.get()


def new_trace_id() -> str:
    return uuid.uuid4().hex


def set_trace_id(trace_id: str) -> contextvars.Token:
    return _trace_id_var.set(trace_id or "-")


def reset_trace_id(token: contextvars.Token) -> None:
    _trace_id_var.reset(token)


class TraceIdFilter(logging.Filter):
    def filter(self, record: logging.LogRecord) -> bool:
        record.trace_id = get_trace_id()
        return True


def configure_logging(service_name: str, log_file: str | Path) -> logging.Logger:
    logger = logging.getLogger(service_name)
    if logger.handlers:
        return logger

    logger.setLevel(logging.INFO)
    formatter = logging.Formatter(
        fmt="%(asctime)s | %(name)s | %(levelname)s | trace=%(trace_id)s | %(message)s"
    )
    trace_filter = TraceIdFilter()

    log_path = Path(log_file)
    log_path.parent.mkdir(parents=True, exist_ok=True)

    file_handler = logging.FileHandler(log_path, encoding="utf-8")
    file_handler.setFormatter(formatter)
    file_handler.addFilter(trace_filter)

    stream_handler = logging.StreamHandler()
    stream_handler.setFormatter(formatter)
    stream_handler.addFilter(trace_filter)

    logger.addHandler(file_handler)
    logger.addHandler(stream_handler)
    logger.propagate = False
    return logger
