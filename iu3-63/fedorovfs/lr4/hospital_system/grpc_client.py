# клиент для общения service a и service b

import grpc
import uuid
import logging
import hospital_pb2
import hospital_pb2_grpc
from exceptions import HospitalException

class ReferenceGrpcClient:
    def __init__(self, host='localhost', port=50051):
        self.channel = grpc.insecure_channel(f'{host}:{port}')
        self.stub = hospital_pb2_grpc.ReferenceServiceStub(self.channel)
        self._trace_counter = 1

    def _generate_trace_metadata(self):
        current_trace_id = self._trace_counter
        self._trace_counter += 1
        logging.info(f"[TraceID: {current_trace_id}] Отправка запроса в Service B...")
        return (('x-trace-id', str(current_trace_id)),)

    def get_ward_info(self, ward_id: int):
        metadata = self._generate_trace_metadata()
        try:
            request = hospital_pb2.GetWardRequest(ward_id=ward_id)
            response = self.stub.GetWard(request, metadata=metadata)
            return response
        except grpc.RpcError as e:
            #обработка недоступности Service B
            if e.code() == grpc.StatusCode.UNAVAILABLE:
                raise HospitalException("Service B временно недоступен. Попробуйте позже.")
            raise HospitalException(f"Ошибка gRPC: {e.details()}")

    def get_doctor_info(self, doctor_id: int):
        metadata = self._generate_trace_metadata()
        try:
            request = hospital_pb2.GetDoctorRequest(doctor_id=doctor_id)
            response = self.stub.GetDoctor(request, metadata=metadata)
            return response
        except grpc.RpcError as e:
            if e.code() == grpc.StatusCode.UNAVAILABLE:
                raise HospitalException("Справочный сервис (Service B) временно недоступен. Попробуйте позже.")
            raise HospitalException(f"Ошибка gRPC: {e.details()}")