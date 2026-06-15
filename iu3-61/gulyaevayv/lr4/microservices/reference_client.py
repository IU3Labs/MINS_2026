import grpc

from microservices import grpc_imports  # noqa: F401
from microservices.config import REFERENCE_ADDRESS
from microservices.mappers import medicine_to_proto
from microservices.trace import metadata_with_trace

import pharmacy_pb2
import pharmacy_pb2_grpc


class ReferenceServiceUnavailable(Exception):
    pass


class ReferenceClient:
    def __init__(self, address=REFERENCE_ADDRESS):
        self.address = address
        self.channel = grpc.insecure_channel(address)
        self.stub = pharmacy_pb2_grpc.ReferenceServiceStub(self.channel)

    def add_medicine(self, medicine, trace_id):
        request = pharmacy_pb2.AddMedicineRequest(medicine=medicine_to_proto(medicine))
        return self._call(self.stub.AddMedicine, request, trace_id)

    def list_medicines(self, trace_id):
        return self._call(self.stub.ListMedicines, pharmacy_pb2.Empty(), trace_id)

    def search_medicines(self, query, trace_id):
        request = pharmacy_pb2.SearchRequest(query=query)
        return self._call(self.stub.SearchMedicines, request, trace_id)

    def validate_sale(self, medicine_name, quantity, has_prescription, trace_id):
        request = pharmacy_pb2.ValidateSaleRequest(
            medicine_name=medicine_name,
            quantity=quantity,
            has_prescription=has_prescription,
        )
        return self._call(self.stub.ValidateSale, request, trace_id)

    def decrease_quantity(self, medicine_name, quantity, trace_id):
        request = pharmacy_pb2.UpdateQuantityRequest(
            medicine_name=medicine_name,
            quantity=quantity,
        )
        return self._call(self.stub.DecreaseMedicineQuantity, request, trace_id)

    def check_expiration(self, days, trace_id):
        request = pharmacy_pb2.ExpirationRequest(days=days)
        return self._call(self.stub.CheckExpiration, request, trace_id)

    def remove_expired(self, trace_id):
        return self._call(self.stub.RemoveExpired, pharmacy_pb2.Empty(), trace_id)

    def _call(self, method, request, trace_id):
        try:
            return method(
                request,
                timeout=3,
                metadata=metadata_with_trace(trace_id),
            )
        except grpc.RpcError as error:
            if error.code() in (
                grpc.StatusCode.UNAVAILABLE,
                grpc.StatusCode.DEADLINE_EXCEEDED,
            ):
                raise ReferenceServiceUnavailable(
                    f"Reference Service is unavailable at {self.address}"
                ) from error
            raise
