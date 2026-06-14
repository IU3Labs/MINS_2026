from __future__ import annotations

from abc import ABC, abstractmethod
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


class PatientRepository(ABC):
    @abstractmethod
    def add(self, patient: Patient) -> None: ...

    @abstractmethod
    def get(self, patient_id: str) -> Patient: ...

    @abstractmethod
    def list(self) -> Sequence[Patient]: ...


class WardRepository(ABC):
    @abstractmethod
    def add(self, ward: Ward) -> None: ...

    @abstractmethod
    def get(self, ward_id: str) -> Ward: ...

    @abstractmethod
    def list(self) -> Sequence[Ward]: ...


class DoctorRepository(ABC):
    @abstractmethod
    def add(self, doctor: Doctor) -> None: ...

    @abstractmethod
    def get(self, doctor_id: str) -> Doctor: ...

    @abstractmethod
    def list(self) -> Sequence[Doctor]: ...


class AppointmentRepository(ABC):
    @abstractmethod
    def add(self, appointment: Appointment) -> None: ...

    @abstractmethod
    def get(self, appointment_id: str) -> Appointment: ...

    @abstractmethod
    def list(self) -> Sequence[Appointment]: ...

    @abstractmethod
    def list_by_patient(self, patient_id: str) -> Sequence[Appointment]: ...

    @abstractmethod
    def list_by_doctor(self, doctor_id: str) -> Sequence[Appointment]: ...


class DiagnosisRepository(ABC):
    @abstractmethod
    def add(self, diagnosis: DiagnosisRecord) -> None: ...

    @abstractmethod
    def get(self, diagnosis_id: str) -> DiagnosisRecord: ...

    @abstractmethod
    def list(self) -> Sequence[DiagnosisRecord]: ...

    @abstractmethod
    def list_by_patient(self, patient_id: str) -> Sequence[DiagnosisRecord]: ...


class PrescriptionRepository(ABC):
    @abstractmethod
    def add(self, prescription: Prescription) -> None: ...

    @abstractmethod
    def get(self, prescription_id: str) -> Prescription: ...

    @abstractmethod
    def list(self) -> Sequence[Prescription]: ...

    @abstractmethod
    def list_by_patient(self, patient_id: str) -> Sequence[Prescription]: ...


class AdmissionRepository(ABC):
    @abstractmethod
    def add(self, admission: Admission) -> None: ...

    @abstractmethod
    def get(self, admission_id: str) -> Admission: ...

    @abstractmethod
    def list(self) -> Sequence[Admission]: ...

    @abstractmethod
    def list_by_patient(self, patient_id: str) -> Sequence[Admission]: ...

    @abstractmethod
    def list_by_ward(self, ward_id: str) -> Sequence[Admission]: ...

    @abstractmethod
    def update(self, admission: Admission) -> None: ...