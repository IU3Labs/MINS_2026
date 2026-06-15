from repositories.sale_repository import SaleRepository


class SaleService:
    def __init__(self, repository=None):
        self.repository = repository or SaleRepository()
        self.sales = self.repository.load_all()

    def load_sales(self):
        self.sales = self.repository.load_all()

    def save_sales(self):
        self.repository.save_all(self.sales)

    def add_sale(self, sale):
        self.sales.append(sale)

    def get_sales(self):
        return self.sales
