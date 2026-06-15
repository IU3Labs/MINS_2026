from datetime import datetime


class Sale:
    def __init__(self, medicine_name, quantity, summary, prescription):
        self.medicine_name = medicine_name
        self.quantity = quantity
        self.summary = summary
        self.prescription = prescription
        self.date = datetime.now()

    def __str__(self):
        prescription = "Да" if self.prescription else "Нет"

        return (
            f"Лекарство: {self.medicine_name}, "
            f"Количество: {self.quantity}, "
            f"Сумма покупки: {self.summary}, "
            f"Дата: {self.date.strftime('%Y-%m-%d %H:%M')}, "
            f"Рецепт: {prescription}"
        )
