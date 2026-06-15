from datetime import date


class Medicine:
    def __init__(self, name, manufacturer, price, expiration_date, prescription_required, quantity):
        self.name = name
        self.manufacturer = manufacturer
        self.price = price
        self.expiration_date = expiration_date
        self.prescription_required = prescription_required
        self.quantity = quantity

    def is_expired(self):
        return self.expiration_date < date.today()

    def __str__(self):
        prescription = "Да" if self.prescription_required else "Нет"

        return (
            f"Название: {self.name}\n"
            f"Производитель: {self.manufacturer}\n"
            f"Цена: {self.price}\n"
            f"Срок годности: {self.expiration_date}\n"
            f"Рецепт требуется: {prescription}\n"
            f"Количество: {self.quantity}\n"
        )
