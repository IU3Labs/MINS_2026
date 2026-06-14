
import sys
from pathlib import Path
import grpc

sys.path.append(str(Path(__file__).resolve().parents[1]))

from grpc_contract import reference_pb2, reference_pb2_grpc


class ReferenceClient:
    def __init__(self):
        self.channel = grpc.insecure_channel("localhost:50051")
        self.stub = reference_pb2_grpc.ReferenceValidatorStub(self.channel)

    def validate_ids(self, trace_id: str, patient_id: str, doctor_id: str, ward_id: str = ""):
        try:
            request = reference_pb2.ValidateRequest(
                trace_id=trace_id,
                patient_id=patient_id,
                doctor_id=doctor_id,
                ward_id=ward_id,
            )
            return self.stub.ValidateIds(request)

        except grpc.RpcError as e:
            self._handle_rpc_error(e)

    def get_patient_name(self, trace_id: str, patient_id: str):
        try:
            request = reference_pb2.IdRequest(trace_id=trace_id, id=patient_id)
            response = self.stub.GetPatientName(request)
            return response.value

        except grpc.RpcError as e:
            self._handle_rpc_error(e)

    def get_doctor_name(self, trace_id: str, doctor_id: str):
        try:
            request = reference_pb2.IdRequest(trace_id=trace_id, id=doctor_id)
            response = self.stub.GetDoctorName(request)
            return response.value

        except grpc.RpcError as e:
            self._handle_rpc_error(e)

    def get_ward_number(self, trace_id: str, ward_id: str):
        try:
            request = reference_pb2.IdRequest(trace_id=trace_id, id=ward_id)
            response = self.stub.GetWardNumber(request)
            return response.value

        except grpc.RpcError as e:
            self._handle_rpc_error(e)

    def _handle_rpc_error(self, e: grpc.RpcError):
        code = e.code()

        if code == grpc.StatusCode.UNAVAILABLE:
            raise RuntimeError("gRPC STATUS 14 UNAVAILABLE: Reference Service недоступен")

        elif code == grpc.StatusCode.INVALID_ARGUMENT:
            raise RuntimeError("gRPC STATUS 3 INVALID_ARGUMENT: Переданы неверные параметры")

        elif code == grpc.StatusCode.NOT_FOUND:
            raise RuntimeError("gRPC STATUS 5 NOT_FOUND: Запрашиваемая сущность не найдена")

        elif code == grpc.StatusCode.INTERNAL:
            raise RuntimeError("gRPC STATUS 13 INTERNAL: Внутренняя ошибка Reference Service")

        elif code == grpc.StatusCode.DEADLINE_EXCEEDED:
            raise RuntimeError("gRPC STATUS 4 DEADLINE_EXCEEDED: Таймаут ожидания ответа")

        else:
            raise RuntimeError(f"gRPC STATUS {code.value[0]} {code.name}: Необработанная ошибка")