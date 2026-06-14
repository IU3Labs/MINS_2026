
from concurrent import futures
import grpc
import sys
from pathlib import Path

sys.path.append(str(Path(__file__).resolve().parents[1]))

from grpc_contract import reference_pb2, reference_pb2_grpc

from hospital_system.storage.json_storage import JsonStorage
from hospital_system.repositories.json_repos import (
    JsonPatientRepository,
    JsonDoctorRepository,
    JsonWardRepository,
)


def grpc_fail(context, code, trace_id, message):
    print(f"[TRACE {trace_id}] STATUS {code.value[0]} {code.name}: {message}")
    context.set_code(code)
    context.set_details(message)


def grpc_ok(trace_id, message):
    print(f"[TRACE {trace_id}] STATUS 0 OK: {message}")


class ReferenceValidatorServicer(reference_pb2_grpc.ReferenceValidatorServicer):
    def __init__(self):
        root = Path(__file__).resolve().parents[1]
        data_dir = root / "data"
        storage = JsonStorage(data_dir)

        self.patient_repo = JsonPatientRepository(storage)
        self.doctor_repo = JsonDoctorRepository(storage)
        self.ward_repo = JsonWardRepository(storage)

    def ValidateIds(self, request, context): #это 
        print(f"[TRACE {request.trace_id}] ValidateIds запрос")

        if not request.patient_id or not request.doctor_id:
            grpc_fail(context, grpc.StatusCode.INVALID_ARGUMENT, request.trace_id, "Empty ids")
            return reference_pb2.ValidateResponse()

        try:
            patient_exists = self.patient_repo.get(request.patient_id) is not None
            doctor_exists = self.doctor_repo.get(request.doctor_id) is not None

            ward_exists = True
            if request.ward_id:
                ward_exists = self.ward_repo.get(request.ward_id) is not None

            if not patient_exists or not doctor_exists or not ward_exists:
                grpc_fail(context, grpc.StatusCode.NOT_FOUND, request.trace_id, "Entity not found")
            else:
                grpc_ok(request.trace_id, "ValidateIds success")

            return reference_pb2.ValidateResponse(
                patient_exists=patient_exists,
                doctor_exists=doctor_exists,
                ward_exists=ward_exists,
                message="ok",
            )

        except Exception:
            grpc_fail(context, grpc.StatusCode.INTERNAL, request.trace_id, "Internal server error")
            return reference_pb2.ValidateResponse()

    def GetPatientName(self, request, context):
        print(f"[TRACE {request.trace_id}] GetPatientName")

        try:
            patient = self.patient_repo.get(request.id)
            if not patient:
                grpc_fail(context, grpc.StatusCode.NOT_FOUND, request.trace_id, "Patient not found")
                return reference_pb2.NameResponse()

            grpc_ok(request.trace_id, "Patient found")
            return reference_pb2.NameResponse(value=patient.full_name)

        except Exception:
            grpc_fail(context, grpc.StatusCode.INTERNAL, request.trace_id, "Internal server error")
            return reference_pb2.NameResponse()

    def GetDoctorName(self, request, context):
        print(f"[TRACE {request.trace_id}] GetDoctorName")

        try:
            doctor = self.doctor_repo.get(request.id)
            if not doctor:
                grpc_fail(context, grpc.StatusCode.NOT_FOUND, request.trace_id, "Doctor not found")
                return reference_pb2.NameResponse()

            grpc_ok(request.trace_id, "Doctor found")
            return reference_pb2.NameResponse(value=doctor.full_name)

        except Exception:
            grpc_fail(context, grpc.StatusCode.INTERNAL, request.trace_id, "Internal server error")
            return reference_pb2.NameResponse()

    def GetWardNumber(self, request, context):
        print(f"[TRACE {request.trace_id}] GetWardNumber")

        try:
            ward = self.ward_repo.get(request.id)
            if not ward:
                grpc_fail(context, grpc.StatusCode.NOT_FOUND, request.trace_id, "Ward not found")
                return reference_pb2.NameResponse()

            grpc_ok(request.trace_id, "Ward found")
            return reference_pb2.NameResponse(value=ward.number)

        except Exception:
            grpc_fail(context, grpc.StatusCode.INTERNAL, request.trace_id, "Internal server error")
            return reference_pb2.NameResponse()


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    reference_pb2_grpc.add_ReferenceValidatorServicer_to_server(
        ReferenceValidatorServicer(), server
    )
    server.add_insecure_port("[::]:50051")
    server.start()
    print("Reference Service started on port 50051")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()