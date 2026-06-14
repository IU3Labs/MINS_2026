from datetime import datetime
from typing import List, Optional

from exceptions import (
    WardFullException,
    InvalidScheduleException,
    DoctorNotFoundException,
    AppointmentNotFoundException,
    ValidationError,
    PatientNotFoundException,
    WardNotFoundException,
    AlreadyHospitalizedException,
    PatientNotHospitalizedException,
    InvalidCapacityException,
    DuplicateWardNumberException,
    InvalidPatientStateException,
)
from models import Patient, Ward, Appointment, Doctor
from notification_event_types import NotificationEventType
from notifications import EventManager, NotificationEvent
from patient_states import (
    DISCHARGED,
    HOSPITALIZED,
    NOT_HOSPITALIZED,
    SCHEDULED_FOR_APPOINTMENT,
    HospitalizedState,
    ScheduledForAppointmentState,
)
from repositories import Repository


class PatientService:
    """Сервис работы с пациентами"""

    def __init__(
        self,
        patient_repo: Repository,
        ward_repo: Repository,
        event_manager: Optional[EventManager] = None,
    ) -> None:
        self.patient_repo = patient_repo
        self.ward_repo = ward_repo
        self._events = event_manager
        self._next_patient_id = 1

    def _notify(self, event: NotificationEvent) -> None:
        if self._events is not None:
            self._events.notify(event)

    def _patient_state_changed(self, patient: Patient, old_label: str, reason: str) -> None:
        self._notify(
            NotificationEvent(
                event_type=NotificationEventType.PATIENT_STATE_CHANGED,
                message=f"Пациент {patient.id}: {old_label} -> {patient.state.label} ({reason})",
                payload={
                    "patient_id": patient.id,
                    "patient_name": patient.name,
                    "old_label": old_label,
                    "new_label": patient.state.label,
                    "reason": reason,
                },
            )
        )

    def _get_patioent_id(self) -> int:
        patient_id = self._next_patient_id
        self._next_patient_id += 1
        return patient_id

    def register_patient(self, name: str, age: int) -> Patient:
        if not name or not isinstance(name, str):
            raise ValidationError(field_name="name", value=name, reason="Имя должно быть непустой строкой")
        if not isinstance(age, int) or age < 0 or age > 150:
            raise ValidationError(field_name="age", value=age, reason="Возраст должен быть числом от 0 до 150")

        patient = Patient(id=self._get_patioent_id(), name=name, age=age, state=NOT_HOSPITALIZED)
        self.patient_repo.add(patient)
        return patient

    def admit_to_ward(self, patient_id: int, ward_id: int) -> None:
        patient = self.patient_repo.get_by_id(patient_id)
        if not patient:
            raise PatientNotFoundException(patient_id=patient_id)

        if isinstance(patient.state, HospitalizedState):
            raise AlreadyHospitalizedException(patient_id)

        if not patient.state.can_be_admitted_to_ward():
            raise InvalidPatientStateException(
                f"Нельзя госпитализировать: текущий статус {patient.state.label}"
            )

        ward = self.ward_repo.get_by_id(ward_id)
        if not ward:
            raise WardNotFoundException(ward_id=ward_id)

        if patient.ward_id is not None:
            raise AlreadyHospitalizedException(patient_id)

        if len(ward.current_patients) >= ward.capacity:
            raise WardFullException(
                ward_number=ward.number,
                capacity=ward.capacity,
                current_count=len(ward.current_patients),
            )

        old_label = patient.state.label
        ward.current_patients.append(patient.id)
        patient.ward_id = ward.id
        patient.state = HOSPITALIZED

        self.ward_repo.update(ward)
        self.patient_repo.update(patient)

        self._patient_state_changed(patient, old_label, "госпитализация")
        self._notify(
            NotificationEvent(
                event_type=NotificationEventType.HOSPITALIZED,
                message=f"Пациент {patient.name} размещён в палате №{ward.number}",
                payload={
                    "patient_id": patient.id,
                    "patient_name": patient.name,
                    "ward_id": ward.id,
                    "ward_number": ward.number,
                },
            )
        )

    def discharge_patient(self, patient_id: int) -> None:
        patient = self.patient_repo.get_by_id(patient_id)
        if not patient:
            raise PatientNotFoundException(patient_id)

        if not isinstance(patient.state, HospitalizedState):
            raise PatientNotHospitalizedException(patient_id)

        if patient.ward_id is None:
            raise PatientNotHospitalizedException(patient_id)

        ward = self.ward_repo.get_by_id(patient.ward_id)
        if ward:
            if patient_id in ward.current_patients:
                ward.current_patients.remove(patient_id)
            self.ward_repo.update(ward)

        old_label = patient.state.label
        patient.ward_id = None
        patient.state = DISCHARGED
        self.patient_repo.update(patient)
        self._patient_state_changed(patient, old_label, "выписка")


class AppointmentService:
    """Логика работы с приёмами и диагнозами"""

    def __init__(
        self,
        appointment_repo: Repository,
        patient_repo: Repository,
        doctor_repo: Repository,
        event_manager: Optional[EventManager] = None,
    ) -> None:
        self.appointment_repo = appointment_repo
        self.patient_repo = patient_repo
        self.doctor_repo = doctor_repo
        self._events = event_manager
        self._next_appointment_id = 1

    def _emit(self, event: NotificationEvent) -> None:
        if self._events is not None:
            self._events.notify(event)

    def _patient_state_changed(self, patient: Patient, old_label: str, reason: str) -> None:
        self._emit(
            NotificationEvent(
                event_type=NotificationEventType.PATIENT_STATE_CHANGED,
                message=f"Пациент {patient.id}: {old_label} -> {patient.state.label} ({reason})",
                payload={
                    "patient_id": patient.id,
                    "patient_name": patient.name,
                    "old_label": old_label,
                    "new_label": patient.state.label,
                    "reason": reason,
                },
            )
        )

    def _generate_appointment_id(self) -> int:
        appointment_id = self._next_appointment_id
        self._next_appointment_id += 1
        return appointment_id

    def schedule_appointment(self, patient_id: int, doctor_id: int, dt: datetime) -> Appointment:
        patient = self.patient_repo.get_by_id(patient_id)
        if not patient:
            raise PatientNotFoundException(patient_id=patient_id)

        if not patient.state.can_schedule_appointment():
            raise InvalidPatientStateException(
                f"Запись невозможна при статусе {patient.state.label} "
                f"(ожидается {NOT_HOSPITALIZED.label} или {DISCHARGED.label})."
            )

        doctor = self.doctor_repo.get_by_id(doctor_id)
        if not doctor:
            raise DoctorNotFoundException(doctor_id=doctor_id)

        if dt < datetime.now():
            raise InvalidScheduleException(
                reason="Нельзя записать на прошедшее время",
                datetime_value=dt.isoformat(),
            )

        old_label = patient.state.label
        patient.state = SCHEDULED_FOR_APPOINTMENT
        self.patient_repo.update(patient)
        self._patient_state_changed(patient, old_label, "запись на приём")

        appointment = Appointment(
            id=self._generate_appointment_id(),
            patient_id=patient_id,
            doctor_id=doctor_id,
            date_time=dt,
        )
        self.appointment_repo.add(appointment)
        return appointment

    def complete_appointment(self, appointment_id: int, diagnosis: str, prescription: str) -> None:
        appointment = self.appointment_repo.get_by_id(appointment_id)
        if not appointment:
            raise AppointmentNotFoundException(appointment_id=appointment_id)

        doctor = self.doctor_repo.get_by_id(appointment.doctor_id)
        if not doctor:
            raise DoctorNotFoundException(appointment.doctor_id)

        patient = self.patient_repo.get_by_id(appointment.patient_id)
        if not patient:
            raise PatientNotFoundException(appointment.patient_id)

        if not patient.state.can_complete_appointment():
            raise InvalidPatientStateException(
                f"Завершить приём нельзя при статусе «{patient.state.label}»"
            )

        appointment.diagnosis = diagnosis
        appointment.prescription = prescription
        patient.diagnosis_history.append(f"{diagnosis} ({appointment.date_time.date()})")
        self.appointment_repo.update(appointment)

        if isinstance(patient.state, ScheduledForAppointmentState):
            # после амбулаторного приёма возвращаемся в не госпитализирован
            old_label = patient.state.label
            patient.state = NOT_HOSPITALIZED
            self.patient_repo.update(patient)
            self._patient_state_changed(patient, old_label, "завершение приёма (без госпитализации)")

        elif isinstance(patient.state, HospitalizedState):
            if patient.ward_id is None:
                raise PatientNotHospitalizedException(patient.id)
            self.patient_repo.update(patient)

        self._emit(
            NotificationEvent(
                event_type=NotificationEventType.APPOINTMENT_COMPLETED,
                message=f"Приём завершён для {patient.name}",
                payload={
                    "patient_id": patient.id,
                    "patient_name": patient.name,
                    "diagnosis": diagnosis,
                    "prescription": prescription,
                    "appointment_date": str(appointment.date_time),
                },
            )
        )


class DoctorService:
    """Логика работы с врачами"""

    def __init__(self, doctor_repo: Repository) -> None:
        self.doctor_repo = doctor_repo
        self._next_doctor_id = 1

    def _generate_doctor_id(self) -> int:
        doctor_id = self._next_doctor_id
        self._next_doctor_id += 1
        return doctor_id

    def add_doctor(self, name: str, specialization: str) -> Doctor:
        doctor = Doctor(id=self._generate_doctor_id(), name=name, specialization=specialization)
        self.doctor_repo.add(doctor)
        return doctor

    def update_doctor(
        self,
        doctor_id: int,
        name: Optional[str] = None,
        specialization: Optional[str] = None,
    ) -> Doctor:
        doctor = self.doctor_repo.get_by_id(doctor_id)
        if doctor is None:
            raise DoctorNotFoundException(doctor_id=doctor_id)

        if name is not None and (not isinstance(name, str) or not name.strip()):
            raise ValidationError(
                field_name="name",
                value=name,
                reason="Имя врача должно быть непустой строкой",
            )
        if specialization is not None and (not isinstance(specialization, str) or not specialization.strip()):
            raise ValidationError(
                field_name="specialization",
                value=specialization,
                reason="Специализация должна быть заполнена",
            )

        if name:
            doctor.name = name
        if specialization:
            doctor.specialization = specialization

        self.doctor_repo.update(doctor)
        return doctor

    def delete_doctor(self, doctor_id: int) -> None:
        doctor = self.doctor_repo.get_by_id(doctor_id)
        if not doctor:
            raise DoctorNotFoundException(doctor_id=doctor_id)
        self.doctor_repo.delete(doctor_id)

    def list_doctors(self) -> List[Doctor]:
        return self.doctor_repo.get_all()


class WardService:
    """Логика работы с палатами"""

    def __init__(self, ward_repo: Repository) -> None:
        self.ward_repo = ward_repo
        self._next_ward_id = 1

    def _generate_ward_id(self) -> int:
        ward_id = self._next_ward_id
        self._next_ward_id += 1
        return ward_id

    def add_ward(self, number: int, capacity: int) -> Ward:
        if capacity <= 0:
            raise InvalidCapacityException(capacity)
        all_wards = self.ward_repo.get_all()
        for w in all_wards:
            if w.number == number:
                raise DuplicateWardNumberException(number)
        ward = Ward(id=self._generate_ward_id(), number=number, capacity=capacity)
        self.ward_repo.add(ward)
        return ward

    def get_ward(self, ward_id: Optional[int]) -> Ward | None:
        if ward_id is None:
            return None
        return self.ward_repo.get_by_id(ward_id)

    def list_wards(self) -> List[Ward]:
        return self.ward_repo.get_all()
