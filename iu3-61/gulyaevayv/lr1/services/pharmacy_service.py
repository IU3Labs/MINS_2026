import csv
from datetime import date, timedelta
from models.medicine import Medicine
from exceptions.pharmacy_exceptions import *


class PharmacyService:

    def __init__(self):
        self.medicines = []
        self.file = "data/medicines.csv"
        self.load_medicines()

    def load_medicines(self):

        try:
            with open(self.file, newline='', encoding="utf-8") as f:
                reader = csv.reader(f)

                for row in reader:
                    name, manufacturer, price, expiration, prescription, quantity = row

                    medicine = Medicine(
                        name,
                        manufacturer,
                        float(price),
                        date.fromisoformat(expiration),
                        prescription == "True",
                        int(quantity)
                    )

                    self.medicines.append(medicine)

        except FileNotFoundError:
            pass

    def save_medicines(self):

        with open(self.file, "w", newline='', encoding="utf-8") as f:

            writer = csv.writer(f)

            for med in self.medicines:

                writer.writerow([
                    med.name,
                    med.manufacturer,
                    med.price,
                    med.expiration_date,
                    med.prescription_required,
                    med.quantity
                ])

    def add_medicine(self, name, manufacturer, price, expiration_date, prescription, quantity):

        medicine = Medicine(
            name,
            manufacturer,
            price,
            expiration_date,
            prescription,
            quantity
        )

        self.medicines.append(medicine)

    def get_all_medicines(self):
        return self.medicines

    def find_medicine(self, name, exact=True):

        results = []

        name = name.lower()

        for med in self.medicines:

            if exact:
                if med.name.lower() == name:
                    return med
            else:
                if name in med.name.lower() or name in med.manufacturer.lower():
                    results.append(med)

        if exact:
            raise MedicineNotFoundException("Лекарство не найдено")

        return results

    def sell_medicine(self, name, quantity, has_prescription):

        medicine = self.find_medicine(name)

        if medicine.is_expired():
            raise ExpiredMedicineException("Лекарство просрочено")

        if medicine.prescription_required and not has_prescription:
            raise PrescriptionRequiredException("Нужен рецепт")

        if medicine.quantity < quantity:
            raise NotEnoughMedicineException("Недостаточно лекарства")

        medicine.quantity -= quantity

        # self.save_medicines()

        return medicine

    # def check_expired(self):

    #     expired = []

    #     for med in self.medicines:
    #         if med.expiration_date < date.today():
    #             expired.append(med)

    #     return expired
    
    def check_expiration_status(self, days=30):

        expired = []
        expiring_soon = []

        today = date.today()
        limit = today + timedelta(days=days)

        for med in self.medicines:

            if med.expiration_date < today:
                expired.append(med)

            elif today <= med.expiration_date <= limit:
                expiring_soon.append(med)

        return expired, expiring_soon
    
    def remove_expired_medicines(self):

        expired = []

        for med in self.medicines:
            if med.is_expired():
                expired.append(med)

        for med in expired:
            self.medicines.remove(med)

        # self.save_medicines()

        return expired
