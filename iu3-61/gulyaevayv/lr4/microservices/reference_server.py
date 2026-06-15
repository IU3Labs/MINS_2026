import logging
from concurrent import futures

import grpc

from exceptions.pharmacy_exceptions import MedicineNotFoundException, PharmacyException
from services.pharmacy_service import PharmacyService
from microservices import grpc_imports  # noqa: F401
from microservices.config import REFERENCE_ADDRESS
from microservices.mappers import medicine_to_proto, proto_to_medicine
from microservices.trace import trace_id_from_context

import pharmacy_pb2
import pharmacy_pb2_grpc


logging.basicConfig(
    filename="reference_service.log",
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
)


class ReferenceServiceServicer(pharmacy_pb2_grpc.ReferenceServiceServicer):
    def __init__(self):
        self.pharmacy_service = PharmacyService()

    def AddMedicine(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s AddMedicine name=%s", trace_id, request.medicine.name)

        if not request.medicine.name.strip():
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Medicine name is required")
        if request.medicine.price <= 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Medicine price must be positive")
        if request.medicine.quantity < 0:
            context.abort(
                grpc.StatusCode.INVALID_ARGUMENT,
                "Medicine quantity cannot be negative",
            )

        try:
            medicine = proto_to_medicine(request.medicine)
        except ValueError as error:
            logging.warning("trace_id=%s invalid medicine data: %s", trace_id, error)
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, str(error))

        self.pharmacy_service.medicines.append(medicine)
        self.pharmacy_service.save_medicines()
        return pharmacy_pb2.OperationResult(ok=True, message="Medicine added")

    def ListMedicines(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s ListMedicines", trace_id)

        return pharmacy_pb2.MedicineList(
            medicines=[
                medicine_to_proto(medicine)
                for medicine in self.pharmacy_service.get_all_medicines()
            ]
        )

    def SearchMedicines(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s SearchMedicines query=%s", trace_id, request.query)

        medicines = self.pharmacy_service.find_medicine(request.query, exact=False)
        return pharmacy_pb2.MedicineList(
            medicines=[medicine_to_proto(medicine) for medicine in medicines]
        )

    def ValidateSale(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info(
            "trace_id=%s ValidateSale medicine=%s quantity=%s",
            trace_id,
            request.medicine_name,
            request.quantity,
        )

        if not request.medicine_name.strip():
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Medicine name is required")
        if request.quantity <= 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Quantity must be positive")

        medicine = self._find_medicine_or_abort(request.medicine_name, context, trace_id)

        if medicine.is_expired():
            context.abort(grpc.StatusCode.FAILED_PRECONDITION, "Medicine is expired")
        if medicine.prescription_required and not request.has_prescription:
            context.abort(grpc.StatusCode.PERMISSION_DENIED, "Prescription is required")
        if medicine.quantity < request.quantity:
            context.abort(
                grpc.StatusCode.RESOURCE_EXHAUSTED,
                "Not enough medicine in stock",
            )

        return pharmacy_pb2.ValidateSaleResponse(
            ok=True,
            message="Sale is valid",
            medicine=medicine_to_proto(medicine),
        )

    def DecreaseMedicineQuantity(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info(
            "trace_id=%s DecreaseMedicineQuantity medicine=%s quantity=%s",
            trace_id,
            request.medicine_name,
            request.quantity,
        )

        if not request.medicine_name.strip():
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Medicine name is required")
        if request.quantity <= 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Quantity must be positive")

        medicine = self._find_medicine_or_abort(request.medicine_name, context, trace_id)
        if medicine.quantity < request.quantity:
            context.abort(
                grpc.StatusCode.RESOURCE_EXHAUSTED,
                "Not enough medicine in stock",
            )

        medicine.quantity -= request.quantity
        self.pharmacy_service.save_medicines()
        return pharmacy_pb2.OperationResult(ok=True, message="Quantity updated")

    def CheckExpiration(self, request, context):
        trace_id = trace_id_from_context(context)
        days = request.days or 30
        logging.info("trace_id=%s CheckExpiration days=%s", trace_id, days)

        expired, expiring_soon = self.pharmacy_service.check_expiration_status(days)
        return pharmacy_pb2.ExpirationStatus(
            expired=[medicine_to_proto(medicine) for medicine in expired],
            expiring_soon=[medicine_to_proto(medicine) for medicine in expiring_soon],
        )

    def RemoveExpired(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s RemoveExpired", trace_id)

        removed = self.pharmacy_service.remove_expired_medicines()
        self.pharmacy_service.save_medicines()
        return pharmacy_pb2.MedicineList(
            medicines=[medicine_to_proto(medicine) for medicine in removed]
        )

    def _find_medicine_or_abort(self, medicine_name, context, trace_id):
        try:
            return self.pharmacy_service.find_medicine(medicine_name)
        except MedicineNotFoundException as error:
            logging.warning("trace_id=%s medicine not found: %s", trace_id, medicine_name)
            context.abort(grpc.StatusCode.NOT_FOUND, str(error))
        except PharmacyException as error:
            logging.error("trace_id=%s reference error: %s", trace_id, error)
            context.abort(grpc.StatusCode.INTERNAL, str(error))


def serve(address=REFERENCE_ADDRESS):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pharmacy_pb2_grpc.add_ReferenceServiceServicer_to_server(
        ReferenceServiceServicer(),
        server,
    )
    server.add_insecure_port(address)
    server.start()
    logging.info("Reference Service started on %s", address)
    print(f"Reference Service started on {address}")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
