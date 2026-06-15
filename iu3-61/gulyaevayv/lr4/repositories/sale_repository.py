from datetime import datetime

from models.sale import Sale
from repositories.csv_repository import CsvRepository


class SaleRepository:
    def __init__(self, file_path="data/sales.csv"):
        self.csv_repository = CsvRepository(file_path)

    def load_all(self):
        sales = []

        for row in self.csv_repository.read_rows():
            name, quantity, summary, prescription, sale_date = row
            sale = Sale(name, int(quantity), float(summary), prescription == "True")
            sale.date = datetime.fromisoformat(sale_date)
            sales.append(sale)

        return sales

    def save_all(self, sales):
        rows = []

        for sale in sales:
            rows.append(
                [
                    sale.medicine_name,
                    sale.quantity,
                    sale.summary,
                    sale.prescription,
                    sale.date,
                ]
            )

        self.csv_repository.write_rows(rows)
