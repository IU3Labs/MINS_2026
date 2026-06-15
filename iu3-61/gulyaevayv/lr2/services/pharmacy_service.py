from datetime import date, timedelta

from exceptions.pharmacy_exceptions import (
    ExpiredMedicineException,
    NotEnoughMedicineException,
    PrescriptionRequiredException,
)
from models.medicine import Medicine
from repositories.medicine_repository import MedicineRepository
from strategies.search_strategy import (
    ExactMedicineSearchStrategy,
    MedicineSearchStrategy,
    PartialMedicineSearchStrategy,
)


class PharmacyService:
    def __init__(self, repository=None, event_manager=None, search_strategy=None):
        self.repository = repository or MedicineRepository()
        self.event_manager = event_manager
        self.search_strategy = search_strategy or ExactMedicineSearchStrategy()
        self.medicines = self.repository.load_all()

    def load_medicines(self):
        self.medicines = self.repository.load_all()

    def save_medicines(self):
        self.repository.save_all(self.medicines)

    def add_medicine(self, name, manufacturer, price, expiration_date, prescription, quantity):
        medicine = Medicine(
            name,
            manufacturer,
            price,
            expiration_date,
            prescription,
            quantity,
        )

        self.medicines.append(medicine)
        self._notify("medicine_added", medicine.name)

    def get_all_medicines(self):
        return self.medicines

    def set_search_strategy(self, search_strategy: MedicineSearchStrategy):
        self.search_strategy = search_strategy

    def find_medicine(self, name, exact=True, search_strategy=None):
        if search_strategy is not None:
            strategy = search_strategy
        elif exact:
            strategy = self.search_strategy
        else:
            strategy = PartialMedicineSearchStrategy()

        return strategy.search(self.medicines, name)

    def sell_medicine(self, name, quantity, has_prescription):
        medicine = self.find_medicine(name)

        if medicine.is_expired():
            raise ExpiredMedicineException("Лекарство просрочено")

        if medicine.prescription_required and not has_prescription:
            raise PrescriptionRequiredException("Нужен рецепт")

        if medicine.quantity < quantity:
            raise NotEnoughMedicineException("Недостаточно лекарства")

        medicine.quantity -= quantity
        self._notify("medicine_sold", f"{medicine.name}, {quantity}")

        return medicine

    def check_expiration_status(self, days=30):
        expired = []
        expiring_soon = []

        today = date.today()
        limit = today + timedelta(days=days)

        for medicine in self.medicines:
            if medicine.expiration_date < today:
                expired.append(medicine)
            elif today <= medicine.expiration_date <= limit:
                expiring_soon.append(medicine)

        return expired, expiring_soon

    def remove_expired_medicines(self):
        expired = []

        for medicine in self.medicines:
            if medicine.is_expired():
                expired.append(medicine)

        for medicine in expired:
            self.medicines.remove(medicine)
            self._notify("medicine_removed", medicine.name)

        return expired

    def _notify(self, event_name, payload):
        if self.event_manager:
            self.event_manager.notify(event_name, payload)
