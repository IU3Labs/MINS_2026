from __future__ import annotations

import sys
from pathlib import Path
sys.path.append(str(Path(__file__).resolve().parents[2]))

from datetime import datetime
from typing import Sequence
from uuid import uuid4

from hospital_system.domain.entities import Admission
from hospital_system.repositories.interfaces import AdmissionRepository
from hospital_system.utils import new_id
from core_service.reference_client import ReferenceClient

class AdmissionService:
    def __init__(self, patients, wards, admissions: AdmissionRepository):
        self._admissions = admissions
        self._reference_client = ReferenceClient()

    def admit(self, patient_id: str, ward_id: str, admitted_at: datetime) -> Admission:
        trace_id = uuid4().hex[:8]
        print(f"[TRACE {trace_id}] Core Service отправляет запрос в Reference Service")

        try:
            response = self._reference_client.validate_ids(trace_id, patient_id, "", ward_id)
        except Exception:
            print("Reference Service недоступен. Госпитализация невозможна.")
            return

        if not response.patient_exists:
            raise Exception("Пациент не найден")

        if not response.ward_exists:
            raise Exception("Палата не найдена")

        admission = Admission(
            id=new_id(),
            patient_id=patient_id,
            ward_id=ward_id,
            admitted_at=admitted_at,
            discharged_at=None,
        )

        self._admissions.add(admission)
        return admission

    def discharge(self, admission_id: str, discharged_at: datetime) -> Admission:
        admission = self._admissions.get(admission_id)

        updated = Admission(
            id=admission.id,
            patient_id=admission.patient_id,
            ward_id=admission.ward_id,
            admitted_at=admission.admitted_at,
            discharged_at=discharged_at,
        )

        self._admissions.update(updated)
        return updated

    def list(self) -> Sequence[Admission]:
        return self._admissions.list()

    def list_by_patient(self, patient_id: str) -> Sequence[Admission]:
        return self._admissions.list_by_patient(patient_id)

    def list_by_ward(self, ward_id: str) -> Sequence[Admission]:
        return self._admissions.list_by_ward(ward_id)

    def get(self, admission_id: str) -> Admission:
        return self._admissions.get(admission_id)