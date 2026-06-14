from __future__ import annotations

from dataclasses import dataclass
from datetime import date, datetime
from typing import Optional

@dataclass (frozen=True)
class Person:
    id: str
    full_name: str
    phone: str

@dataclass (frozen=True)
class Patient(Person):
    birth_date: Optional[date] = None

@dataclass (frozen=True)
class Doctor(Person):
    specialty: str = ""

@dataclass (frozen=True)
class Ward:
    id: str
    number: str
    name: str
    capacity: int
    is_icu: bool = False

@dataclass (frozen=True)
class Appointment:
    id: str
    patient_id: str
    doctor_id: str
    starts_at: datetime
    duration_min: int
    ward_id: Optional[str] = None

@dataclass (frozen=True)
class DiagnosisRecord:
    id: str
    patient_id: str
    doctor_id: str
    diagnosed_at: datetime
    diagnosis: str
    notes: str = ""

@dataclass (frozen=True)
class Prescription:
    id: str
    patient_id: str
    doctor_id: str
    prescribed_at: datetime
    medicine: str
    dosage: str
    instructions: str
    days: int

@dataclass (frozen=True)
class Admission:
    id: str
    patient_id: str
    ward_id: str
    admitted_at: datetime
    discharged_at: Optional[datetime] = None