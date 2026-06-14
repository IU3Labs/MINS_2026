import logging
import grpc
from concurrent import futures
import hospital_pb2
import hospital_pb2_grpc

from repositories import InMemoryRepository
from services import DoctorService, WardService

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


class ReferenceServicer(hospital_pb2_grpc.ReferenceServiceServicer):
    def __init__(self):
        self.doctor_repo = InMemoryRepository()
        self.ward_repo = InMemoryRepository()
        self.doctor_service = DoctorService(self.doctor_repo)
        self.ward_service = WardService(self.ward_repo)
        
        self.doctor_service.add_doctor("Доктор Хаус", "Терапевт")
        self.ward_service.add_ward(101, 2)

    def _get_trace_id(self, context):
        """Извлекает Trace ID из метаданных запроса"""
        metadata = dict(context.invocation_metadata())
        return metadata.get('x-trace-id', 'no-trace-id')

    def GetDoctor(self, request, context):
        trace_id = self._get_trace_id(context)
        logging.info(f"[TraceID: {trace_id}] Запрос проверки врача ID={request.doctor_id}")

        doctor = self.doctor_repo.get_by_id(request.doctor_id)
        if doctor:
            return hospital_pb2.DoctorResponse(
                exists=True, id=doctor.id, name=doctor.name, specialization=doctor.specialization
            )
        return hospital_pb2.DoctorResponse(exists=False)

    def GetWard(self, request, context):
        trace_id = self._get_trace_id(context)
        logging.info(f"[TraceID: {trace_id}] Запрос проверки палаты ID={request.ward_id}")
        ward = self.ward_repo.get_by_id(request.ward_id)
        if ward:
            return hospital_pb2.WardResponse(
                exists=True, id=ward.id, number=ward.number, capacity=ward.capacity
            )
        return hospital_pb2.WardResponse(exists=False)

    def AddDoctor(self, request, context):
        trace_id = self._get_trace_id(context)
        logging.info(f"[TraceID: {trace_id}] Запрос на добавление врача: {request.name}")

        doctor = self.doctor_service.add_doctor(request.name, request.specialization)
        return hospital_pb2.DoctorResponse(
            exists=True, id=doctor.id, name=doctor.name, specialization=doctor.specialization
        )

    def ListDoctors(self, request, context):
        trace_id = self._get_trace_id(context)
        logging.info(f"[TraceID: {trace_id}] Запрос списка всех врачей")

        doctors = self.doctor_service.list_doctors()
        response = hospital_pb2.DoctorListResponse()
        for doc in doctors:
            response.doctors.add(exists=True, id=doc.id, name=doc.name, specialization=doc.specialization)
        return response


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    hospital_pb2_grpc.add_ReferenceServiceServicer_to_server(ReferenceServicer(), server)
    server.add_insecure_port('[::]:50051')
    logging.info("Service B (Reference) запущен на порту 50051...")
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()