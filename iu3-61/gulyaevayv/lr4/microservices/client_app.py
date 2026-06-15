import logging

import grpc

from exceptions.pharmacy_exceptions import InputException
from models.medicine import Medicine
from utils.input_handler import InputHandler
from microservices import grpc_imports  # noqa: F401
from microservices.config import CORE_ADDRESS
from microservices.mappers import medicine_to_proto, proto_to_medicine
from microservices.trace import metadata_with_trace, new_trace_id

import pharmacy_pb2
import pharmacy_pb2_grpc


logging.basicConfig(
    filename="client_app.log",
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
)


class MicroservicePharmacyApp:
    def __init__(self, core_address=CORE_ADDRESS):
        self.core_channel = grpc.insecure_channel(core_address)
        self.core_stub = pharmacy_pb2_grpc.CoreServiceStub(self.core_channel)

    def run(self):
        while True:
            print("1 Add medicine")
            print("2 Show medicines")
            print("3 Search")
            print("4 Sell")
            print("5 Check expiration")
            print("6 Remove expired")
            print("7 Sales")
            print("8 Friday discount")
            print("0 Exit")

            choice = input("Choice: ")
            if choice == "0":
                break

            trace_id = new_trace_id()
            logging.info("trace_id=%s menu choice=%s", trace_id, choice)
            self.execute(choice, trace_id)

    def execute(self, choice, trace_id):
        try:
            if choice == "1":
                self.add_medicine(trace_id)
            elif choice == "2":
                self.show_medicines(trace_id)
            elif choice == "3":
                self.search(trace_id)
            elif choice == "4":
                self.sell(trace_id)
            elif choice == "5":
                self.check_expiration(trace_id)
            elif choice == "6":
                self.remove_expired(trace_id)
            elif choice == "7":
                self.show_sales(trace_id)
            elif choice == "8":
                self.calculate_friday_discount(trace_id)
            else:
                print("Unknown command")
        except grpc.RpcError as error:
            self.print_grpc_error(error)
        except InputException as error:
            logging.warning("trace_id=%s invalid input: %s", trace_id, error)
            print(f"{grpc.StatusCode.INVALID_ARGUMENT.name}: {error}")

    def add_medicine(self, trace_id):
        medicine = Medicine(
            InputHandler.input_string("Name: "),
            InputHandler.input_string("Manufacturer: "),
            InputHandler.input_float("Price: "),
            InputHandler.input_date("Expiration date (YYYY-MM-DD): "),
            InputHandler.input_bool("Prescription required? (y/n): "),
            InputHandler.input_int("Quantity: "),
        )
        request = pharmacy_pb2.AddMedicineRequest(medicine=medicine_to_proto(medicine))
        result = self.core_stub.AddMedicine(
            request,
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        print(result.message)

    def show_medicines(self, trace_id):
        response = self.core_stub.ListMedicines(
            pharmacy_pb2.Empty(),
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        for medicine in response.medicines:
            print(proto_to_medicine(medicine))

    def search(self, trace_id):
        query = InputHandler.input_string("Search: ")
        response = self.core_stub.SearchMedicines(
            pharmacy_pb2.SearchRequest(query=query),
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        for medicine in response.medicines:
            print(proto_to_medicine(medicine))

    def sell(self, trace_id):
        request = pharmacy_pb2.SellMedicineRequest(
            medicine_name=InputHandler.input_string("Name: "),
            quantity=InputHandler.input_int("Quantity: "),
            has_prescription=InputHandler.input_bool("Has prescription? (y/n): "),
        )
        response = self.core_stub.SellMedicine(
            request,
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        print(f"{response.message}. Total: {response.total}")

    def check_expiration(self, trace_id):
        response = self.core_stub.CheckExpiration(
            pharmacy_pb2.ExpirationRequest(days=30),
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        for medicine in response.expired:
            print("Expired:", proto_to_medicine(medicine))
        for medicine in response.expiring_soon:
            print("Expiring soon:", proto_to_medicine(medicine))

    def remove_expired(self, trace_id):
        response = self.core_stub.RemoveExpired(
            pharmacy_pb2.Empty(),
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        for medicine in response.medicines:
            print("Removed:", proto_to_medicine(medicine))

    def show_sales(self, trace_id):
        response = self.core_stub.ListSales(
            pharmacy_pb2.Empty(),
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        for sale in response.sales:
            print(
                f"Medicine: {sale.medicine_name}, Quantity: {sale.quantity}, "
                f"Total: {sale.summary}, Date: {sale.date}, Prescription: {sale.prescription}"
            )

    def calculate_friday_discount(self, trace_id):
        request = pharmacy_pb2.DiscountRequest(
            medicine_name=InputHandler.input_string("Name: "),
            quantity=InputHandler.input_int("Quantity: "),
        )
        response = self.core_stub.CalculateFridayDiscount(
            request,
            timeout=3,
            metadata=metadata_with_trace(trace_id),
        )
        print(response.message)

    def print_grpc_error(self, error):
        code = error.code()
        details = error.details()
        if code == grpc.StatusCode.UNAVAILABLE and "failed to connect" in details:
            print("Core Service is unavailable. Try again later.")
            return

        print(f"{code.name}: {details}")


if __name__ == "__main__":
    MicroservicePharmacyApp().run()
