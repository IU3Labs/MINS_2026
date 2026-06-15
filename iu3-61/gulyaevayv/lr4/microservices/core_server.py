import logging
from concurrent import futures

import grpc

from antipatterns.friday_discount_calculator import FridayDiscountCalculator
from models.sale import Sale
from services.sale_service import SaleService
from microservices import grpc_imports  # noqa: F401
from microservices.config import CORE_ADDRESS
from microservices.mappers import proto_to_medicine, sale_to_proto
from microservices.reference_client import ReferenceClient, ReferenceServiceUnavailable
from microservices.trace import trace_id_from_context

import pharmacy_pb2
import pharmacy_pb2_grpc


logging.basicConfig(
    filename="core_service.log",
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
)


class CoreServiceServicer(pharmacy_pb2_grpc.CoreServiceServicer):
    def __init__(self, reference_client=None):
        self.reference_client = reference_client or ReferenceClient()
        self.sale_service = SaleService()
        self.discount_calculator = FridayDiscountCalculator()

    def AddMedicine(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s AddMedicine name=%s", trace_id, request.medicine.name)

        try:
            return self.reference_client._call(
                self.reference_client.stub.AddMedicine,
                request,
                trace_id,
            )
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)

    def ListMedicines(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s ListMedicines", trace_id)

        try:
            return self.reference_client.list_medicines(trace_id)
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)

    def SearchMedicines(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s SearchMedicines query=%s", trace_id, request.query)

        try:
            return self.reference_client.search_medicines(request.query, trace_id)
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)

    def CheckExpiration(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s CheckExpiration days=%s", trace_id, request.days)

        try:
            return self.reference_client.check_expiration(request.days, trace_id)
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)

    def RemoveExpired(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s RemoveExpired", trace_id)

        try:
            return self.reference_client.remove_expired(trace_id)
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)

    def SellMedicine(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info(
            "trace_id=%s SellMedicine medicine=%s quantity=%s",
            trace_id,
            request.medicine_name,
            request.quantity,
        )

        try:
            validation = self.reference_client.validate_sale(
                request.medicine_name,
                request.quantity,
                request.has_prescription,
                trace_id,
            )

            update_result = self.reference_client.decrease_quantity(
                request.medicine_name,
                request.quantity,
                trace_id,
            )

            total = validation.medicine.price * request.quantity
            sale = Sale(
                request.medicine_name,
                request.quantity,
                total,
                request.has_prescription,
            )
            self.sale_service.add_sale(sale)
            self.sale_service.save_sales()
            logging.info("trace_id=%s sale saved total=%s", trace_id, total)

            return pharmacy_pb2.SellMedicineResponse(
                ok=True,
                message="Sale completed",
                total=total,
            )
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)

    def ListSales(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info("trace_id=%s ListSales", trace_id)

        return pharmacy_pb2.SaleList(
            sales=[sale_to_proto(sale) for sale in self.sale_service.get_sales()]
        )

    def CalculateFridayDiscount(self, request, context):
        trace_id = trace_id_from_context(context)
        logging.info(
            "trace_id=%s CalculateFridayDiscount medicine=%s quantity=%s",
            trace_id,
            request.medicine_name,
            request.quantity,
        )

        try:
            medicines_response = self.reference_client.list_medicines(trace_id)
            medicines = [
                proto_to_medicine(medicine)
                for medicine in medicines_response.medicines
            ]
            report = self.discount_calculator.calculate(
                medicines,
                request.medicine_name,
                request.quantity,
            )
            return pharmacy_pb2.DiscountResponse(ok=True, message=report)
        except ReferenceServiceUnavailable as error:
            logging.error("trace_id=%s %s", trace_id, error)
            context.abort(
                grpc.StatusCode.UNAVAILABLE,
                "Reference Service is unavailable. Try again later.",
            )
        except grpc.RpcError as error:
            self._abort_with_rpc_error(context, trace_id, error)
        except Exception as error:
            logging.error("trace_id=%s discount error: %s", trace_id, error)
            context.abort(grpc.StatusCode.INTERNAL, str(error))

    def _abort_with_rpc_error(self, context, trace_id, error):
        code = error.code()
        details = error.details()
        logging.warning(
            "trace_id=%s downstream grpc error code=%s details=%s",
            trace_id,
            code.name,
            details,
        )
        context.abort(code, details)


def serve(address=CORE_ADDRESS):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pharmacy_pb2_grpc.add_CoreServiceServicer_to_server(
        CoreServiceServicer(),
        server,
    )
    server.add_insecure_port(address)
    server.start()
    logging.info("Core Service started on %s", address)
    print(f"Core Service started on {address}")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
