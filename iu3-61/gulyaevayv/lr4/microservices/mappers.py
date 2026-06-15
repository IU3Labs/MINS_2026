from datetime import date

from models.medicine import Medicine
from models.sale import Sale

from microservices import grpc_imports  # noqa: F401
import pharmacy_pb2


def medicine_to_proto(medicine):
    return pharmacy_pb2.Medicine(
        name=medicine.name,
        manufacturer=medicine.manufacturer,
        price=medicine.price,
        expiration_date=medicine.expiration_date.isoformat(),
        prescription_required=medicine.prescription_required,
        quantity=medicine.quantity,
    )


def proto_to_medicine(message):
    return Medicine(
        message.name,
        message.manufacturer,
        message.price,
        date.fromisoformat(message.expiration_date),
        message.prescription_required,
        message.quantity,
    )


def sale_to_proto(sale):
    return pharmacy_pb2.Sale(
        medicine_name=sale.medicine_name,
        quantity=sale.quantity,
        summary=sale.summary,
        prescription=sale.prescription,
        date=sale.date.isoformat(),
    )
