class PharmacyException(Exception):
    pass


class MedicineNotFoundException(PharmacyException):
    pass


class ExpiredMedicineException(PharmacyException):
    pass


class PrescriptionRequiredException(PharmacyException):
    pass


class NotEnoughMedicineException(PharmacyException):
    pass


class InputException(PharmacyException):
    pass