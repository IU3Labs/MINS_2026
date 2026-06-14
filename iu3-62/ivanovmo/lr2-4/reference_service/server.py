import grpc
import logging
import sys
import os
from concurrent import futures

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
import reference_pb2_grpc
from reference_service.reference_impl import ReferenceServicer

def setup_logging():
    logging.basicConfig(
        level=logging.INFO,
        format='[%(asctime)s] %(levelname)s - %(message)s'
    )

def serve():
    setup_logging()
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    reference_pb2_grpc.add_ReferenceServiceServicer_to_server(ReferenceServicer(), server)
    server.add_insecure_port('[::]:50052')
    logging.info("Reference Service (B) started on port 50052")
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    serve()