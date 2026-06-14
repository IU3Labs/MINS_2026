from __future__ import annotations

from datetime import datetime
from typing import Sequence
from uuid import uuid4

from hospital_system.domain.entities import DiagnosisRecord
from hospital_system.repositories.interfaces import DiagnosisRepository
from hospital_system.utils import new_id
from core_service.reference_client import ReferenceClient

class DiagnosisService:
    def __init__(self, patients, doctors, diagnoses: DiagnosisRepository):
        self._diagnoses = diagnoses
        self._reference_client = ReferenceClient()

    def add(
        self,
        patient_id: str,
        doctor_id: str,
        diagnosed_at: datetime,
        diagnosis: str,
        notes: str = "",
    ) -> DiagnosisRecord:

        trace_id = uuid4().hex[:8]
        response = self._reference_client.validate_ids(trace_id, patient_id, doctor_id, "")

        if not response.patient_exists:
            raise Exception("Пациент не найден в Reference Service")

        if not response.doctor_exists:
            raise Exception("Врач не найден в Reference Service")

        record = DiagnosisRecord(
            id=new_id(),
            patient_id=patient_id,
            doctor_id=doctor_id,
            diagnosed_at=diagnosed_at,
            diagnosis=diagnosis,
            notes=notes.strip(),
        )

        self._diagnoses.add(record)
        return record

    def list(self) -> Sequence[DiagnosisRecord]:
        return self._diagnoses.list()

    def list_by_patient(self, patient_id: str) -> Sequence[DiagnosisRecord]:
        return self._diagnoses.list_by_patient(patient_id)