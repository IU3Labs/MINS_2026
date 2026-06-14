"""Visitor"""

from __future__ import annotations

import json
from abc import ABC, abstractmethod
from typing import Any, Dict, List

from models import Appointment, Doctor, Patient, Ward


def _patient_row(p: Patient) -> Dict[str, Any]:
    return {
        "id": p.id,
        "name": p.name,
        "age": p.age,
        "state": p.state.label,
        "state_key": p.state.key,
        "ward_id": p.ward_id,
        "diagnosis_history": list(p.diagnosis_history),
    }


def _doctor_row(d: Doctor) -> Dict[str, Any]:
    return {"id": d.id, "name": d.name, "specialization": d.specialization}


def _ward_row(w: Ward) -> Dict[str, Any]:
    return {
        "id": w.id,
        "number": w.number,
        "capacity": w.capacity,
        "current_patients": list(w.current_patients),
    }


def _appointment_row(a: Appointment) -> Dict[str, Any]:
    return {
        "id": a.id,
        "patient_id": a.patient_id,
        "doctor_id": a.doctor_id,
        "date_time": a.date_time.isoformat() if a.date_time else None,
        "diagnosis": a.diagnosis,
        "prescription": a.prescription,
    }


class HospitalExportVisitor(ABC):
    """"""
    @abstractmethod
    def visit_patients(self, patients: List[Patient]) -> str:
        pass

    @abstractmethod
    def visit_doctors(self, doctors: List[Doctor]) -> str:
        pass

    @abstractmethod
    def visit_wards(self, wards: List[Ward]) -> str:
        pass

    @abstractmethod
    def visit_appointments(self, appointments: List[Appointment]) -> str:
        pass


class TxtExportVisitor(HospitalExportVisitor):
    def visit_patients(self, patients: List[Patient]) -> str:
        lines = ["Пациенты"]
        for p in patients:
            ward = p.ward_id if p.ward_id is not None else "—"
            hist = "; ".join(p.diagnosis_history) if p.diagnosis_history else "—"
            lines.append(
                f"ID {p.id} | {p.name}, {p.age} лет | {p.state.label} | палата: {ward} | история: {hist}"
            )
        return "\n".join(lines) + "\n"

    def visit_doctors(self, doctors: List[Doctor]) -> str:
        lines = ["Врачи"]
        for d in doctors:
            lines.append(f"ID {d.id} | {d.name} | {d.specialization}")
        return "\n".join(lines) + "\n"

    def visit_wards(self, wards: List[Ward]) -> str:
        lines = ["Палаты"]
        for w in wards:
            lines.append(
                f"ID {w.id} | №{w.number} | мест: {w.capacity} | пациенты (ID): {w.current_patients}"
            )
        return "\n".join(lines) + "\n"

    def visit_appointments(self, appointments: List[Appointment]) -> str:
        lines = ["Приёмы"]
        for a in appointments:
            lines.append(
                f"ID {a.id} | пациент {a.patient_id}, врач {a.doctor_id} | {a.date_time} | "
                f"диагноз: {a.diagnosis} | рецепт: {a.prescription}"
            )
        return "\n".join(lines) + "\n"


class JsonExportVisitor(HospitalExportVisitor):
    def visit_patients(self, patients: List[Patient]) -> str:
        data = {"entity": "patients", "items": [_patient_row(p) for p in patients]}
        return json.dumps(data, ensure_ascii=False, indent=2) + "\n"

    def visit_doctors(self, doctors: List[Doctor]) -> str:
        data = {"entity": "doctors", "items": [_doctor_row(d) for d in doctors]}
        return json.dumps(data, ensure_ascii=False, indent=2) + "\n"

    def visit_wards(self, wards: List[Ward]) -> str:
        data = {"entity": "wards", "items": [_ward_row(w) for w in wards]}
        return json.dumps(data, ensure_ascii=False, indent=2) + "\n"

    def visit_appointments(self, appointments: List[Appointment]) -> str:
        data = {"entity": "appointments", "items": [_appointment_row(a) for a in appointments]}
        return json.dumps(data, ensure_ascii=False, indent=2) + "\n"


def save_export(content: str, filename: str) -> None:
    with open(filename, "w", encoding="utf-8") as f:
        f.write(content)
