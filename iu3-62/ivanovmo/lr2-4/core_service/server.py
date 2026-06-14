import grpc
import logging
import sys
import os
from concurrent import futures
from contextvars import ContextVar

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
import core_pb2_grpc
import reference_pb2_grpc
from core_service.core_impl import CoreServicer, trace_id_var
from core_service.repository import OrderRepository

logger = logging.getLogger(__name__)
trace_id_var: ContextVar[str] = ContextVar('trace_id', default='no-trace')

class TraceIDInterceptor(grpc.ServerInterceptor):
    def intercept_service(self, continuation, handler_call_details):
        metadata = handler_call_details.invocation_metadata
        trace_id = None
        if metadata:
            for key, value in metadata:
                if key == 'x-trace-id':
                    trace_id = value
                    break
        if not trace_id:
            import uuid
            trace_id = str(uuid.uuid4())[:8]
        token = trace_id_var.set(trace_id)
        try:
            return continuation(handler_call_details)
        finally:
            trace_id_var.reset(token)

def setup_logging():
    class TraceIdFilter(logging.Filter):
        def filter(self, record):
            record.trace_id = trace_id_var.get()
            return True
    logging.basicConfig(level=logging.INFO,
                        format='[%(asctime)s] [%(trace_id)s] %(levelname)s - %(message)s')
    for handler in logging.getLogger().handlers:
        handler.addFilter(TraceIdFilter())

def serve():
    setup_logging()
    # Connect to Reference Service
    ref_channel = grpc.insecure_channel('localhost:50052')
    ref_stub = reference_pb2_grpc.ReferenceServiceStub(ref_channel)
    repo = OrderRepository()
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10),
                         interceptors=[TraceIDInterceptor()])
    core_pb2_grpc.add_CoreServiceServicer_to_server(CoreServicer(ref_stub, repo), server)
    server.add_insecure_port('[::]:50051')
    logger.info("Core Service (A) started on port 50051")
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    serve()