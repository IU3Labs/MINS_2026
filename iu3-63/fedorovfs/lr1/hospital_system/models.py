from dataclasses import dataclass, field
from typing import List, Optional
from datetime import datetime


@dataclass
class Numerable:
    id: int


@dataclass
class Person(Numerable):
    name: str


@dataclass
class Patient(Person):
    status: str
    age: int
    ward_id: Optional[int] = None
    diagnosis_history: List[str] = field(default_factory=list)


@dataclass
class Doctor(Person):
    specialization: str


@dataclass
class Ward(Numerable):
    number: int
    capacity: int
    current_patients: List[int] = field(default_factory=list)


@dataclass
class Appointment:
    id: int
    patient_id: int
    doctor_id: int
    date_time: datetime
    diagnosis: Optional[str] = None
    prescription: Optional[str] = None