from __future__ import annotations

from datetime import date, datetime
from typing import Sequence

from hospital_system.domain.entities import (
    Admission,
    Appointment,
    DiagnosisRecord,
    Doctor,
    Patient,
    Prescription,
    Ward,
)
from hospital_system.exceptions import NotFoundError
from hospital_system.repositories.interfaces import (
    AdmissionRepository,
    AppointmentRepository,
    DiagnosisRepository,
    DoctorRepository,
    PatientRepository,
    PrescriptionRepository,
    WardRepository,
)
from hospital_system.storage.json_storage import JsonStorage
from hospital_system.utils import DATE_FMT, DT_FMT


def _date_to_str(d: date | None) -> str | None:
    return d.strftime(DATE_FMT) if d is not None else None


def _str_to_date(s: str | None) -> date | None:
    if s in (None, ""):
        return None
    return datetime.strptime(s, DATE_FMT).date()


def _dt_to_str(dt: datetime | None) -> str | None:
    return dt.strftime(DT_FMT) if dt is not None else None


def _str_to_dt(s: str | None) -> datetime | None:
    if s in (None, ""):
        return None
    return datetime.strptime(s, DT_FMT)


class JsonPatientRepository(PatientRepository):
    _file = "patients.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, patient: Patient) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {
                "id": patient.id,
                "full_name": patient.full_name,
                "phone": patient.phone,
                "birth_date": _date_to_str(patient.birth_date),
            }
        )
        self._storage.write(self._file, rows)

    def get(self, patient_id: str) -> Patient:
        for r in self._storage.read(self._file):
            if r["id"] == patient_id:
                return Patient(
                    id=r["id"],
                    full_name=r["full_name"],
                    phone=r["phone"],
                    birth_date=_str_to_date(r.get("birth_date")),
                )
        raise NotFoundError(f"Пациент не найден: {patient_id}")

    def list(self) -> Sequence[Patient]:
        rows = self._storage.read(self._file)
        return [
            Patient(
                id=r["id"],
                full_name=r["full_name"],
                phone=r["phone"],
                birth_date=_str_to_date(r.get("birth_date")),
            )
            for r in rows
        ]


class JsonWardRepository(WardRepository):
    _file = "wards.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, ward: Ward) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {
                "id": ward.id,
                "number": ward.number,
                "name": ward.name,
                "capacity": ward.capacity,
                "is_icu": ward.is_icu,
            }
        )
        self._storage.write(self._file, rows)

    def get(self, ward_id: str) -> Ward:
        for r in self._storage.read(self._file):
            if r["id"] == ward_id:
                return Ward(
                    id=r["id"],
                    number=r["number"],
                    name=r["name"],
                    capacity=int(r["capacity"]),
                    is_icu=bool(r.get("is_icu", False)),
                )
        raise NotFoundError(f"Палата не найдена: {ward_id}")

    def list(self) -> Sequence[Ward]:
        rows = self._storage.read(self._file)
        return [
            Ward(
                id=r["id"],
                number=r["number"],
                name=r["name"],
                capacity=int(r["capacity"]),
                is_icu=bool(r.get("is_icu", False)),
            )
            for r in rows
        ]


class JsonDoctorRepository(DoctorRepository):
    _file = "doctors.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, doctor: Doctor) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {"id": doctor.id,
                "full_name": doctor.full_name,
                "specialty": doctor.specialty,
                "phone": doctor.phone,
            }
        )
        self._storage.write(self._file, rows)

    def get(self, doctor_id: str) -> Doctor:
        for r in self._storage.read(self._file):
            if r["id"] == doctor_id:
                return Doctor(
                    id=r["id"],
                    full_name=r["full_name"],
                    specialty=r["specialty"],
                    phone=r.get("phone", ""),
                )
        raise NotFoundError(f"Врач не найден: {doctor_id}")

    def list(self) -> Sequence[Doctor]:
        rows = self._storage.read(self._file)
        return [
            Doctor(
                id=r["id"],
                full_name=r["full_name"],
                specialty=r["specialty"],
                phone=r.get("phone", ""),
            )
            for r in rows
        ]


class JsonAppointmentRepository(AppointmentRepository):
    _file = "appointments.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, appointment: Appointment) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {
                "id": appointment.id,
                "patient_id": appointment.patient_id,
                "doctor_id": appointment.doctor_id,
                "starts_at": _dt_to_str(appointment.starts_at),
                "duration_min": appointment.duration_min,
                "ward_id": appointment.ward_id,
            }
        )
        self._storage.write(self._file, rows)

    def get(self, appointment_id: str) -> Appointment:
        for r in self._storage.read(self._file):
            if r["id"] == appointment_id:
                return Appointment(
                    id=r["id"],
                    patient_id=r["patient_id"],
                    doctor_id=r["doctor_id"],
                    starts_at=_str_to_dt(r["starts_at"]),
                    duration_min=int(r["duration_min"]),
                    ward_id=r.get("ward_id"),
                )
        raise NotFoundError(f"Приём не найден: {appointment_id}")

    def list(self) -> Sequence[Appointment]:
        rows = self._storage.read(self._file)
        return [
            Appointment(
                id=r["id"],
                patient_id=r["patient_id"],
                doctor_id=r["doctor_id"],
                starts_at=_str_to_dt(r["starts_at"]),
                duration_min=int(r["duration_min"]),
                ward_id=r.get("ward_id"),
            )
            for r in rows
        ]

    def list_by_patient(self, patient_id: str) -> Sequence[Appointment]:
        return [a for a in self.list() if a.patient_id == patient_id]

    def list_by_doctor(self, doctor_id: str) -> Sequence[Appointment]:
        return [a for a in self.list() if a.doctor_id == doctor_id]


class JsonDiagnosisRepository(DiagnosisRepository):
    _file = "diagnoses.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, diagnosis: DiagnosisRecord) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {
                "id": diagnosis.id,
                "patient_id": diagnosis.patient_id,
                "doctor_id": diagnosis.doctor_id,
                "diagnosed_at": _dt_to_str(diagnosis.diagnosed_at),
                "diagnosis": diagnosis.diagnosis,
                "notes": diagnosis.notes,
            }
        )
        self._storage.write(self._file, rows)

    def get(self, diagnosis_id: str) -> DiagnosisRecord:
        for r in self._storage.read(self._file):
            if r["id"] == diagnosis_id:
                return DiagnosisRecord(
                    id=r["id"],
                    patient_id=r["patient_id"],
                    doctor_id=r["doctor_id"],
                    diagnosed_at=_str_to_dt(r["diagnosed_at"]),
                    diagnosis=r["diagnosis"],
                    notes=r.get("notes", ""),)
        raise NotFoundError(f"Диагноз не найден: {diagnosis_id}")

    def list(self) -> Sequence[DiagnosisRecord]:
        rows = self._storage.read(self._file)
        return [
            DiagnosisRecord(
                id=r["id"],
                patient_id=r["patient_id"],
                doctor_id=r["doctor_id"],
                diagnosed_at=_str_to_dt(r["diagnosed_at"]),
                diagnosis=r["diagnosis"],
                notes=r.get("notes", ""),
            )
            for r in rows
        ]

    def list_by_patient(self, patient_id: str) -> Sequence[DiagnosisRecord]:
        return [d for d in self.list() if d.patient_id == patient_id]


class JsonPrescriptionRepository(PrescriptionRepository):
    _file = "prescriptions.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, prescription: Prescription) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {
                "id": prescription.id,
                "patient_id": prescription.patient_id,
                "doctor_id": prescription.doctor_id,
                "prescribed_at": _dt_to_str(prescription.prescribed_at),
                "medicine": prescription.medicine,
                "dosage": prescription.dosage,
                "instructions": prescription.instructions,
                "days": prescription.days,
            }
        )
        self._storage.write(self._file, rows)

    def get(self, prescription_id: str) -> Prescription:
        for r in self._storage.read(self._file):
            if r["id"] == prescription_id:
                return Prescription(
                    id=r["id"],
                    patient_id=r["patient_id"],
                    doctor_id=r["doctor_id"],
                    prescribed_at=_str_to_dt(r["prescribed_at"]),
                    medicine=r["medicine"],
                    dosage=r["dosage"],
                    instructions=r["instructions"],
                    days=int(r["days"]),
                )
        raise NotFoundError(f"Рецепт не найден: {prescription_id}")

    def list(self) -> Sequence[Prescription]:
        rows = self._storage.read(self._file)
        return [
            Prescription(
                id=r["id"],
                patient_id=r["patient_id"],
                doctor_id=r["doctor_id"],
                prescribed_at=_str_to_dt(r["prescribed_at"]),
                medicine=r["medicine"],
                dosage=r["dosage"],
                instructions=r["instructions"],
                days=int(r["days"]),
            )
            for r in rows
        ]

    def list_by_patient(self, patient_id: str) -> Sequence[Prescription]:
        return [p for p in self.list() if p.patient_id == patient_id]


class JsonAdmissionRepository(AdmissionRepository):
    _file = "admissions.json"

    def __init__(self, storage: JsonStorage):
        self._storage = storage

    def add(self, admission: Admission) -> None:
        rows = self._storage.read(self._file)
        rows.append(
            {
                "id": admission.id,
                "patient_id": admission.patient_id,
                "ward_id": admission.ward_id,
                "admitted_at": _dt_to_str(admission.admitted_at),
                "discharged_at": _dt_to_str(admission.discharged_at),
            }
        )
        self._storage.write(self._file, rows)

    def get(self, admission_id: str) -> Admission:
        for r in self._storage.read(self._file):
            if r["id"] == admission_id:
                return Admission(
                    id=r["id"],
                    patient_id=r["patient_id"],
                    ward_id=r["ward_id"],
                    admitted_at=_str_to_dt(r["admitted_at"]),
                    discharged_at=_str_to_dt(r.get("discharged_at")),
                )
        raise NotFoundError(f"Госпитализация не найдена: {admission_id}")

    def list(self) -> Sequence[Admission]:
        rows = self._storage.read(self._file)
        return [
            Admission(id=r["id"],
                patient_id=r["patient_id"],
                ward_id=r["ward_id"],
                admitted_at=_str_to_dt(r["admitted_at"]),
                discharged_at=_str_to_dt(r.get("discharged_at")),
            )
            for r in rows
        ]

    def list_by_patient(self, patient_id: str) -> Sequence[Admission]:
        return [a for a in self.list() if a.patient_id == patient_id]

    def list_by_ward(self, ward_id: str) -> Sequence[Admission]:
        return [a for a in self.list() if a.ward_id == ward_id]

    def update(self, admission: Admission) -> None:
        rows = self._storage.read(self._file)
        for i, r in enumerate(rows):
            if r["id"] == admission.id:
                rows[i] = {
                    "id": admission.id,
                    "patient_id": admission.patient_id,
                    "ward_id": admission.ward_id,
                    "admitted_at": _dt_to_str(admission.admitted_at),
                    "discharged_at": _dt_to_str(admission.discharged_at),
                }
                self._storage.write(self._file, rows)
                return
        raise NotFoundError(f"Госпитализация не найдена: {admission.id}")