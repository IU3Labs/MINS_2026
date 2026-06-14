from __future__ import annotations
from hospital_system.services.treatment_cost_service import TreatmentCostService
from pathlib import Path

from hospital_system.cli import CliApp, CliDeps
from hospital_system.repositories.json_repos import (
    JsonAdmissionRepository,
    JsonAppointmentRepository,
    JsonDiagnosisRepository,
    JsonDoctorRepository,
    JsonPatientRepository,
    JsonPrescriptionRepository,
    JsonWardRepository,
)
from hospital_system.services.admission_service import AdmissionService
from hospital_system.services.appointment_service import AppointmentService
from hospital_system.services.diagnosis_service import DiagnosisService
from hospital_system.services.doctor_service import DoctorService
from hospital_system.services.patient_service import PatientService
from hospital_system.services.prescription_service import PrescriptionService
from hospital_system.services.reminder_service import ReminderService
from hospital_system.services.ward_service import WardService
from hospital_system.storage.json_storage import JsonStorage

# 🔥 ДОБАВИЛИ
from hospital_system.observer.console_observer import ConsoleObserver

def build_app(data_dir: Path) -> CliApp:
    storage = JsonStorage(data_dir)
    
    patient_repo = JsonPatientRepository(storage)
    ward_repo = JsonWardRepository(storage)
    doctor_repo = JsonDoctorRepository(storage)
    appointment_repo = JsonAppointmentRepository(storage)
    diagnosis_repo = JsonDiagnosisRepository(storage)
    prescription_repo = JsonPrescriptionRepository(storage)
    admission_repo = JsonAdmissionRepository(storage)

    # 🔥 СОЗДАЕМ OBSERVER ЗДЕСЬ
    observer = ConsoleObserver()

    patients = PatientService(patient_repo)
    wards = WardService(ward_repo)
    doctors = DoctorService(doctor_repo)

    # 🔥 ПЕРЕДАЕМ OBSERVER
    appointments = AppointmentService(
        patient_repo,
        doctor_repo,
        ward_repo,
        appointment_repo,
        observer,
    )

    diagnoses = DiagnosisService(patient_repo, doctor_repo, diagnosis_repo)
    prescriptions = PrescriptionService(patient_repo, doctor_repo, prescription_repo)
    admissions = AdmissionService(patient_repo, ward_repo, admission_repo)
    reminders = ReminderService(appointment_repo)
    treatment_cost = TreatmentCostService()
    deps = CliDeps(
        patients=patients,
        wards=wards,
        doctors=doctors,
        appointments=appointments,
        diagnoses=diagnoses,
        prescriptions=prescriptions,
        admissions=admissions,
        reminders=reminders,
        treatment_cost=treatment_cost,
    )
    return CliApp(deps)

def main() -> None:
    root = Path(__file__).resolve().parents[1]
    data_dir = root / "data"
    app = build_app(data_dir)
    app.run()