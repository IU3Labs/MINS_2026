from exceptions.pharmacy_exceptions import MedicineNotFoundException


class ExactMedicineSearchStrategy:
    def search(self, medicines, query):
        query = query.lower()

        for medicine in medicines:
            if medicine.name.lower() == query:
                return medicine

        raise MedicineNotFoundException("Лекарство не найдено")


class PartialMedicineSearchStrategy:
    def search(self, medicines, query):
        query = query.lower()
        results = []

        for medicine in medicines:
            if query in medicine.name.lower() or query in medicine.manufacturer.lower():
                results.append(medicine)

        return results
