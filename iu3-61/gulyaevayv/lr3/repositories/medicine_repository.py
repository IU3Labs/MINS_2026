from datetime import date

from models.medicine import Medicine
from repositories.csv_repository import CsvRepository


class MedicineRepository:
    def __init__(self, file_path="data/medicines.csv"):
        self.csv_repository = CsvRepository(file_path)

    def load_all(self):
        medicines = []

        for row in self.csv_repository.read_rows():
            name, manufacturer, price, expiration, prescription, quantity = row
            medicines.append(
                Medicine(
                    name,
                    manufacturer,
                    float(price),
                    date.fromisoformat(expiration),
                    prescription == "True",
                    int(quantity),
                )
            )

        return medicines

    def save_all(self, medicines):
        rows = []

        for medicine in medicines:
            rows.append(
                [
                    medicine.name,
                    medicine.manufacturer,
                    medicine.price,
                    medicine.expiration_date,
                    medicine.prescription_required,
                    medicine.quantity,
                ]
            )

        self.csv_repository.write_rows(rows)
