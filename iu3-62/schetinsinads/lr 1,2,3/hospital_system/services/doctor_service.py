from __future__ import annotations

from typing import Sequence

from hospital_system.domain.entities import Doctor
from hospital_system.repositories.interfaces import DoctorRepository
from hospital_system.factories.person_factory import PersonFactory

class DoctorService:
    def __init__(self, repo: DoctorRepository):
        self._repo = repo
        self._factory = PersonFactory()

    def create(self, full_name: str, specialty: str, phone: str = "") -> Doctor:
        doctor = self._factory.create_doctor(full_name, specialty, phone)
        self._repo.add(doctor)
        return doctor

    def list(self) -> Sequence[Doctor]:
        return self._repo.list()

    def get(self, doctor_id: str) -> Doctor:
        return self._repo.get(doctor_id)