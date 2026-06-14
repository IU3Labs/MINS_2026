from __future__ import annotations
from dataclasses import dataclass
from typing import Callable, Dict
from hospital_system.services.error_handler import ErrorHandler
from hospital_system.services.admission_service import AdmissionService
from hospital_system.services.appointment_service import AppointmentService
from hospital_system.services.diagnosis_service import DiagnosisService
from hospital_system.services.doctor_service import DoctorService
from hospital_system.services.patient_service import PatientService
from hospital_system.services.prescription_service import PrescriptionService
from hospital_system.services.reminder_service import ReminderService
from hospital_system.services.ward_service import WardService
from hospital_system.services.treatment_cost_service import TreatmentCostService
from hospital_system.utils import ensure_positive_int, parse_date, parse_datetime


@dataclass
class CliDeps:
    patients: PatientService
    wards: WardService
    doctors: DoctorService
    appointments: AppointmentService
    diagnoses: DiagnosisService
    prescriptions: PrescriptionService
    admissions: AdmissionService
    reminders: ReminderService
    treatment_cost: TreatmentCostService


class CliApp:
    def __init__(self, deps: CliDeps):
        self._d = deps
        self._errors = ErrorHandler()
        self._handlers: Dict[str, Callable[[], None]] = {
            "1": self._add_patient,
            "2": self._list_patients,
            "3": self._add_ward,
            "4": self._list_wards,
            "5": self._add_doctor,
            "6": self._list_doctors,
            "7": self._schedule_appointment,
            "8": self._list_appointments,
            "9": self._add_diagnosis,
            "10": self._list_diagnoses_by_patient,
            "11": self._issue_prescription,
            "12": self._list_prescriptions_by_patient,
            "13": self._admit_patient,
            "14": self._discharge_patient,
            "15": self._list_admissions_by_patient,
            "16": self._show_reminders,
            "17": self._calculate_treatment_cost,
        }

    def run(self) -> None:
        while True:
            print("\n=== Hospital Information System ===")
            print("1) Добавить пациента")
            print("2) Список пациентов")
            print("3) Добавить палату")
            print("4) Список палат")
            print("5) Добавить врача")
            print("6) Список врачей")
            print("7) Назначить прием")
            print("8) Расписание приемов")
            print("9) Добавить диагноз")
            print("10) История диагнозов пациента")
            print("11) Выписать рецепт")
            print("12) Рецепты пациента")
            print("13) Госпитализировать пациента")
            print("14) Выписать пациента")
            print("15) Госпитализации пациента")
            print("16) Напоминания о приемах")
            print("17) Быстрый расчет стоимости лечения")
            print("0) Выход")

            cmd = input("> ").strip()
            if cmd == "0":
                print("Выход.")
                return

            handler = self._handlers.get(cmd)
            if not handler:
                print("Неизвестная команда.")
                continue
            try:
                handler()

            except KeyboardInterrupt:
                print("\nОтменено пользователем.")

            except Exception as e:
                self._errors.handle(e)

    def _add_patient(self) -> None:
        full_name = input("ФИО: ")
        phone = input("Телефон: ")
        birth_raw = input("Дата рождения (YYYY-MM-DD, можно пусто): ").strip()
        birth_date = parse_date(birth_raw) if birth_raw else None
        p = self._d.patients.register(full_name, phone, birth_date)
        print(f"OK. Пациент создан: id={p.id}")

    def _list_patients(self) -> None:
        rows = self._d.patients.list()
        if not rows:
            print("Пусто.")
            return
        print("\nID | ФИО | Телефон | Дата рождения")
        for p in rows:
            b = p.birth_date if p.birth_date else "-"
            print(f"{p.id} | {p.full_name} | {p.phone} | {b}")

    def _add_ward(self) -> None:
        number = input("Номер палаты: ")
        name = input("Название палаты: ")
        capacity = ensure_positive_int(input("Вместимость: "), "Вместимость")
        is_icu = input("Реанимация/интенсивная терапия? (y/n): ").strip().lower() in {"y", "yes", "д", "да"}
        w = self._d.wards.create(number, name, capacity, is_icu)
        print(f"OK. Палата создана: id={w.id}")

    def _list_wards(self) -> None:
        rows = self._d.wards.list()
        if not rows:
            print("Пусто.")
            return
        print("\nID | Номер | Название | Вместимость | ICU")
        for w in rows:
            print(f"{w.id} | {w.number} | {w.name} | {w.capacity} | {w.is_icu}")

    def _add_doctor(self) -> None:
        full_name = input("ФИО врача: ")
        specialty = input("Специальность: ")
        phone = input("Телефон (можно пусто): ")
        d = self._d.doctors.create(full_name, specialty, phone)
        print(f"OK. Врач создан: id={d.id}")

    def _list_doctors(self) -> None:
        rows = self._d.doctors.list()
        if not rows:
            print("Пусто.")
            return
        print("\nID | ФИО | Специальность | Телефон")
        for d in rows:
            phone = d.phone if d.phone else "-"
            print(f"{d.id} | {d.full_name} | {d.specialty} | {phone}")

    def _schedule_appointment(self) -> None:
        patient_id = input("Patient ID: ").strip()
        doctor_id = input("Doctor ID: ").strip()
        ward_id = input("Ward ID (можно пусто): ").strip() or None
        starts_at = parse_datetime(input("Дата/время приема (YYYY-MM-DD HH:MM): "))
        duration = ensure_positive_int(input("Длительность (мин): "), "Длительность")
        a = self._d.appointments.schedule(patient_id, doctor_id, starts_at, duration, ward_id)
        print(f"OK. Прием назначен: id={a.id}")

    def _list_appointments(self) -> None:
        rows = self._d.appointments.list()
        if not rows:
            print("Пусто.")
            return
        print("\nID | Пациент | Врач | Начало | Длительность | Палата")
        for a in sorted(rows, key=lambda x: x.starts_at):
            patient = self._d.patients.get(a.patient_id).full_name
            doctor = self._d.doctors.get(a.doctor_id).full_name
            ward = a.ward_id if a.ward_id else "-"
            print(f"{a.id} | {patient} | {doctor} | {a.starts_at} | {a.duration_min} | {ward}")

    def _add_diagnosis(self) -> None:
        patient_id = input("Patient ID: ").strip()
        doctor_id = input("Doctor ID: ").strip()
        diagnosed_at = parse_datetime(input("Дата/время диагноза (YYYY-MM-DD HH:MM): "))
        diagnosis = input("Диагноз: ")
        notes = input("Примечания (можно пусто): ")
        d = self._d.diagnoses.add(patient_id, doctor_id, diagnosed_at, diagnosis, notes)
        print(f"OK. Диагноз добавлен: id={d.id}")

    def _list_diagnoses_by_patient(self) -> None:
        patient_id = input("Patient ID: ").strip()
        rows = self._d.diagnoses.list_by_patient(patient_id)
        if not rows:
            print("Пусто.")
            return
        patient = self._d.patients.get(patient_id).full_name
        print(f"\nИстория диагнозов пациента: {patient}")
        print("ID | Дата | Врач | Диагноз | Примечания")
        for d in sorted(rows, key=lambda x: x.diagnosed_at):
            doctor = self._d.doctors.get(d.doctor_id).full_name
            notes = d.notes if d.notes else "-"
            print(f"{d.id} | {d.diagnosed_at} | {doctor} | {d.diagnosis} | {notes}")

    def _issue_prescription(self) -> None:
        patient_id = input("Patient ID: ").strip()
        doctor_id = input("Doctor ID: ").strip()
        prescribed_at = parse_datetime(input("Дата/время рецепта (YYYY-MM-DD HH:MM): "))
        medicine = input("Лекарство: ")
        dosage = input("Дозировка: ")
        instructions = input("Инструкция: ")
        days = ensure_positive_int(input("Срок действия/дней: "), "Срок действия")
        p = self._d.prescriptions.issue(
            patient_id,
            doctor_id,
            prescribed_at,
            medicine,
            dosage,
            instructions,
            days,
        )
        print(f"OK. Рецепт выписан: id={p.id}")

    def _list_prescriptions_by_patient(self) -> None:
        patient_id = input("Patient ID: ").strip()
        rows = self._d.prescriptions.list_by_patient(patient_id)
        if not rows:
            print("Пусто.")
            return
        patient = self._d.patients.get(patient_id).full_name
        print(f"\nРецепты пациента: {patient}")
        print("ID | Дата | Врач | Лекарство | Дозировка | Инструкция | Дней")
        for p in sorted(rows, key=lambda x: x.prescribed_at):
            doctor = self._d.doctors.get(p.doctor_id).full_name
            print(
                f"{p.id} | {p.prescribed_at} | {doctor} | {p.medicine} | {p.dosage} | {p.instructions} | {p.days}"
            )

    def _admit_patient(self) -> None:
        patient_id = input("Patient ID: ").strip()
        ward_id = input("Ward ID: ").strip()
        admitted_at = parse_datetime(input("Дата/время поступления (YYYY-MM-DD HH:MM): "))
        a = self._d.admissions.admit(patient_id, ward_id, admitted_at)
        print(f"OK. Госпитализация создана: id={a.id}")

    def _discharge_patient(self) -> None:
        admission_id = input("Admission ID: ").strip()
        discharged_at = parse_datetime(input("Дата/время выписки (YYYY-MM-DD HH:MM): "))
        a = self._d.admissions.discharge(admission_id, discharged_at)
        print(f"OK. Пациент выписан: id={a.id}")

    def _list_admissions_by_patient(self) -> None:
        patient_id = input("Patient ID: ").strip()
        rows = self._d.admissions.list_by_patient(patient_id)
        if not rows:
            print("Пусто.")
            return
        patient = self._d.patients.get(patient_id).full_name
        print(f"\nГоспитализации пациента: {patient}")
        print("ID | Палата | Поступление | Выписка")
        for a in sorted(rows, key=lambda x: x.admitted_at):
            ward = self._d.wards.get(a.ward_id).number
            discharged = a.discharged_at if a.discharged_at else "-"
            print(f"{a.id} | {ward} | {a.admitted_at} | {discharged}")

    def _show_reminders(self) -> None:
        days = ensure_positive_int(input("Показать приемы в течение N дней: "), "N")
        rows = self._d.reminders.upcoming_within(days)
        if not rows:
            print("Нет приемов в выбранном окне.")
            return
        print("\nID | Пациент | Врач | Начало | Осталось дней")
        for appt, left in rows:
            patient = self._d.patients.get(appt.patient_id).full_name
            doctor = self._d.doctors.get(appt.doctor_id).full_name
            print(f"{appt.id} | {patient} | {doctor} | {appt.starts_at} | {left}")
    def _calculate_treatment_cost(self) -> None:
        days = ensure_positive_int(input("Количество дней лечения: "), "Количество дней")
        appointments = ensure_positive_int(input("Количество приемов: "), "Количество приемов")
        diagnoses = ensure_positive_int(input("Количество диагнозов: "), "Количество диагнозов")
        prescriptions = ensure_positive_int(input("Количество рецептов: "), "Количество рецептов")
        icu = input("Была реанимация? (y/n): ").strip().lower() in {"y", "yes", "д", "да"}

        total = self._d.treatment_cost.quick_calculate(
            days,
            appointments,
            diagnoses,
            prescriptions,
            icu,
        )

        print(f"Примерная стоимость лечения: {total} руб.")
