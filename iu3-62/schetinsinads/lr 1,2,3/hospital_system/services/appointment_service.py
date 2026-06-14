from __future__ import annotations

import sys
from pathlib import Path

sys.path.append(str(Path(__file__).resolve().parents[2]))

from datetime import datetime
from typing import Sequence
from uuid import uuid4

from hospital_system.domain.entities import Appointment
from hospital_system.repositories.interfaces import AppointmentRepository
from hospital_system.utils import new_id
from hospital_system.observer.observer import Observer
from core_service.reference_client import ReferenceClient

class AppointmentService:
    def __init__(
        self,
        patients,
        doctors,
        wards,
        appointments: AppointmentRepository,
        observer: Observer,
    ):
        self._appointments = appointments
        self._observer = observer
        self._reference_client = ReferenceClient()

    def schedule(
        self,
        patient_id: str,
        doctor_id: str,
        starts_at: datetime,
        duration_min: int,
        ward_id: str | None = None,
    ) -> Appointment:

        trace_id = uuid4().hex[:8]
        print(f"[TRACE {trace_id}] Core Service -> ValidateIds")

        try:
            response = self._reference_client.validate_ids(
                trace_id,
                patient_id,
                doctor_id,
                ward_id or "",
            )
        except Exception:
            print("Reference Service недоступен. Назначение приема невозможно.")
            return

        if not response.patient_exists:
            raise Exception("Пациент не найден в Reference Service")

        if not response.doctor_exists:
            raise Exception("Врач не найден в Reference Service")

        if ward_id and not response.ward_exists:
            raise Exception("Палата не найдена в Reference Service")

        appointment = Appointment(
            id=new_id(),
            patient_id=patient_id,
            doctor_id=doctor_id,
            starts_at=starts_at,
            duration_min=duration_min,
            ward_id=ward_id,
        )

        self._appointments.add(appointment)
        self._observer.update("appointment_created", appointment.id)

        return appointment

    def list(self) -> Sequence[Appointment]:
        return self._appointments.list()

    def get(self, appointment_id: str) -> Appointment:
        return self._appointments.get(appointment_id)

    def list_by_patient(self, patient_id: str) -> Sequence[Appointment]:
        return self._appointments.list_by_patient(patient_id)

    def list_by_doctor(self, doctor_id: str) -> Sequence[Appointment]:
        return self._appointments.list_by_doctor(doctor_id)