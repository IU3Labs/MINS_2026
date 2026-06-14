from datetime import date
from hospital_system.domain.entities import Patient, Doctor
from hospital_system.utils import new_id

class PersonFactory:
    def create_patient(self, full_name: str, phone: str, birth_date: date | None) -> Patient:
        return Patient(
            id=new_id(),
            full_name=full_name,
            phone=phone,
            birth_date=birth_date,
        )

    def create_doctor(self, full_name: str, specialty: str, phone: str) -> Doctor:
        return Doctor(
            id=new_id(),
            full_name=full_name,
            specialty=specialty,
            phone=phone.strip(),
        )