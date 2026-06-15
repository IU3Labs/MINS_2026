from abc import ABC, abstractmethod

from exceptions.pharmacy_exceptions import MedicineNotFoundException


class MedicineSearchStrategy(ABC):
    @abstractmethod
    def search(self, medicines, query):
        pass


class ExactMedicineSearchStrategy(MedicineSearchStrategy):
    def search(self, medicines, query):
        query = query.lower()

        for medicine in medicines:
            if medicine.name.lower() == query:
                return medicine

        raise MedicineNotFoundException("Лекарство не найдено")


class PartialMedicineSearchStrategy(MedicineSearchStrategy):
    def search(self, medicines, query):
        query = query.lower()
        results = []

        for medicine in medicines:
            if query in medicine.name.lower() or query in medicine.manufacturer.lower():
                results.append(medicine)

        return results
