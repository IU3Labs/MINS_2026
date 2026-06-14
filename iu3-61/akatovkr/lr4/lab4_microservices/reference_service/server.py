from __future__ import annotations

from concurrent import futures
from pathlib import Path

import grpc

from lab4_microservices.common.grpc_utils import TraceIdServerInterceptor
from lab4_microservices.common.trace import configure_logging
from lab4_microservices.generated import reference_service_pb2_grpc
from lab4_microservices.reference_service.repository import JsonReferenceRepository
from lab4_microservices.reference_service.service import ReferenceServiceServicer

DEFAULT_REFERENCE_HOST = "127.0.0.1"
DEFAULT_REFERENCE_PORT = 50052


def serve(host: str = DEFAULT_REFERENCE_HOST, port: int = DEFAULT_REFERENCE_PORT):
    project_root = Path(__file__).resolve().parents[2]
    logger = configure_logging(
        "reference-service",
        project_root / "lab4_microservices" / "logs" / "reference_service.log",
    )
    repository = JsonReferenceRepository(
        project_root / "lab4_microservices" / "data" / "reference_data.json"
    )
    server = grpc.server(
        futures.ThreadPoolExecutor(max_workers=10),
        interceptors=[TraceIdServerInterceptor()],
    )
    reference_service_pb2_grpc.add_ReferenceServiceServicer_to_server(
        ReferenceServiceServicer(repository, logger),
        server,
    )
    address = f"{host}:{port}"
    server.add_insecure_port(address)
    server.start()
    logger.info("Reference Service запущен на %s", address)
    return server
