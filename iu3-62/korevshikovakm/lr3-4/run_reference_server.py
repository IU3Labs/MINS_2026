"""Точка входа сервиса B (справочник меню)"""

from __future__ import annotations

import logging
import os
import signal
import sys
from concurrent import futures

import grpc

from cafe_app.grpc_gen import menu_reference_pb2_grpc
from cafe_app.menu_reference_grpc import MenuReferenceGrpcServicer
from cafe_app.repositories import InMemoryMenuRepository
from cafe_app.trace_logging import setup_logging

log = logging.getLogger(__name__)


def main() -> None:
    setup_logging("service-b")
    port = os.environ.get("CAFE_REFERENCE_PORT", "50051") #ервис В выбирает порт, сначала смотрит переменную окружения, если нет исп. порт
    addr = f"0.0.0.0:{port}" #Формируется адрес сервера. 0.0.0.0 значит: сервер слушает подключения на всех сетевых интерфейсах
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=8)) #Созд. грпс сервер, макс_волкер - сервер может обрабатывать неск запросов параллельно в отдельных потоках
    repo = InMemoryMenuRepository() #Созд. хранилище меню внутри В
    menu_reference_pb2_grpc.add_MenuReferenceServicer_to_server(MenuReferenceGrpcServicer(repo), server) #Подкл. к грпс серверу, add_MenuReferenceServicer_to_server(...) регистрирует методы на сервере.
    server.add_insecure_port(addr) #Сервер начинает слушать указанный адрес без шифрования
    server.start()
    log.info("Сервис B (справочник меню) слушает %s (CAFE_REFERENCE_PORT=%s)", addr, port)

    def stop(*_: object) -> None:
        server.stop(grace=1)
        sys.exit(0)

    signal.signal(signal.SIGINT, stop) #SIGINT — сигнал от Ctrl+C. При нажатии вызов стоп
    if hasattr(signal, "SIGTERM"):
        signal.signal(signal.SIGTERM, stop)
    server.wait_for_termination() #Сервер продолжает работать и ждёт запросы


if __name__ == "__main__":
    main()
