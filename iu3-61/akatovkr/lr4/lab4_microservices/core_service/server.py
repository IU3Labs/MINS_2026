from __future__ import annotations

from concurrent import futures
from pathlib import Path

import grpc

from lab4_microservices.common.grpc_utils import TraceIdServerInterceptor
from lab4_microservices.common.trace import configure_logging
from lab4_microservices.core_service.reference_client import ReferenceClient
from lab4_microservices.core_service.repository import JsonCoreRepository
from lab4_microservices.core_service.service import CoreServiceServicer
from lab4_microservices.generated import core_service_pb2_grpc

DEFAULT_CORE_HOST = "127.0.0.1"
DEFAULT_CORE_PORT = 50051
DEFAULT_REFERENCE_TARGET = "127.0.0.1:50052"


def serve(
    host: str = DEFAULT_CORE_HOST,
    port: int = DEFAULT_CORE_PORT,
    reference_target: str = DEFAULT_REFERENCE_TARGET,
    rule_set: str = "sanpin",
):
    project_root = Path(__file__).resolve().parents[2]
    logger = configure_logging(
        "core-service",
        project_root / "lab4_microservices" / "logs" / "core_service.log",
    )
    repository = JsonCoreRepository(project_root / "lab4_microservices" / "data" / "core_data.json")
    reference_client = ReferenceClient(reference_target)
    server = grpc.server(
        futures.ThreadPoolExecutor(max_workers=10),
        interceptors=[TraceIdServerInterceptor()],
    )
    core_service_pb2_grpc.add_CoreServiceServicer_to_server(
        CoreServiceServicer(repository, reference_client, logger, rule_set=rule_set),
        server,
    )
    address = f"{host}:{port}"
    server.add_insecure_port(address)
    server.start()
    logger.info("Core Service запущен на %s. Reference target=%s, rule_set=%s", address, reference_target, rule_set)
    return server
