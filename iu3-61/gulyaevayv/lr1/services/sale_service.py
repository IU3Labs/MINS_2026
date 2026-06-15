import csv
from models.sale import Sale
from datetime import datetime


class SaleService:

    def __init__(self):

        self.sales = []
        self.file = "data/sales.csv"

        self.load_sales()

    def load_sales(self):

        try:
            with open(self.file, newline='', encoding="utf-8") as f:

                reader = csv.reader(f)

                for row in reader:

                    name, quantity, summary, prescription, date = row

                    sale = Sale(
                        name,
                        int(quantity),
                        summary,
                        prescription == "True"
                    )

                    sale.date = datetime.fromisoformat(date)

                    self.sales.append(sale)

        except FileNotFoundError:
            pass

    def save_sales(self):

        with open(self.file, "w", newline='', encoding="utf-8") as f:

            writer = csv.writer(f)

            for sale in self.sales:

                writer.writerow([
                    sale.medicine_name,
                    sale.quantity,
                    sale.summary,
                    sale.prescription,
                    sale.date
                ])

    def add_sale(self, sale):

        self.sales.append(sale)

        # self.save_sales()

    def get_sales(self):
        return self.sales
