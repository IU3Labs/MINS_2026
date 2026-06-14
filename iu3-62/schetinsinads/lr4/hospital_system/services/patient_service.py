from __future__ import annotations

from datetime import date
from typing import Sequence

from hospital_system.domain.entities import Patient
from hospital_system.repositories.interfaces import PatientRepository
from hospital_system.factories.person_factory import PersonFactory

class PatientService:
    def __init__(self, repo: PatientRepository):
        self._repo = repo
        self._factory = PersonFactory()

    def register(self, full_name: str, phone: str, birth_date: date | None = None) -> Patient:
        patient = self._factory.create_patient(full_name, phone, birth_date)
        self._repo.add(patient)
        return patient

    def list(self) -> Sequence[Patient]:
        return self._repo.list()

    def get(self, patient_id: str) -> Patient:
        return self._repo.get(patient_id)