from __future__ import annotations

import sys
from pathlib import Path
sys.path.append(str(Path(__file__).resolve().parents[2]))

from datetime import datetime
from typing import Sequence
from uuid import uuid4

from hospital_system.domain.entities import Prescription
from hospital_system.repositories.interfaces import PrescriptionRepository
from hospital_system.utils import new_id
from core_service.reference_client import ReferenceClient

class PrescriptionService:
    def __init__(self, patients, doctors, prescriptions: PrescriptionRepository):
        self._prescriptions = prescriptions
        self._reference_client = ReferenceClient()

    def issue(
        self,
        patient_id: str,
        doctor_id: str,
        prescribed_at: datetime,
        medicine: str,
        dosage: str,
        instructions: str,
        days: int,
    ) -> Prescription:

        trace_id = uuid4().hex[:8]
        print(f"[TRACE {trace_id}] Core Service отправляет запрос в Reference Service")

        try:
            response = self._reference_client.validate_ids(trace_id, patient_id, doctor_id, "")
        except Exception:
            print("Reference Service недоступен. Выписка рецепта невозможна.")
            return

        if not response.patient_exists:
            raise Exception("Пациент не найден")

        if not response.doctor_exists:
            raise Exception("Врач не найден")

        prescription = Prescription(
            id=new_id(),
            patient_id=patient_id,
            doctor_id=doctor_id,
            prescribed_at=prescribed_at,
            medicine=medicine,
            dosage=dosage,
            instructions=instructions,
            days=days,
        )

        self._prescriptions.add(prescription)
        return prescription

    def list(self) -> Sequence[Prescription]:
        return self._prescriptions.list()

    def list_by_patient(self, patient_id: str) -> Sequence[Prescription]:
        return self._prescriptions.list_by_patient(patient_id)