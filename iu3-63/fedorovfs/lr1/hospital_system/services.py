from datetime import datetime
from typing import List, Optional

from exceptions import (
    WardFullException,
    InvalidScheduleException,
    DoctorNotFoundException,
    AppointmentNotFoundException,
    ValidationError, PatientNotFoundException, WardNotFoundException, AlreadyHospitalizedException,
    PatientNotHospitalizedException, InvalidCapacityException, DuplicateWardNumberException,
)
from models import Patient, Ward, Appointment, Doctor
from repositories import Repository  # , import InMemoryRepository - нарушение Dependency inversion


class PatientService:
    """Сервис работы с пациентами"""

    def __init__(self, patient_repo: Repository, ward_repo: Repository) -> None:
        # можно нарушить принцип O. open/closed , если сделаем что то типа

        # также можно нарушить dependency inversion, если будем передавать определенный тип репозитория

        self.patient_repo = patient_repo  # регистрация
        self.ward_repo = ward_repo  # госпитализаия
        self._next_patient_id = 1

    def _get_patioent_id(self) -> int:
        patient_id = self._next_patient_id
        self._next_patient_id += 1
        return patient_id

    def register_patient(self, name: str, age: int) -> Patient:
        if not name or not isinstance(name, str):
            raise ValidationError(field_name="name", value=name, reason="Имя должно быть непустой строкой")
        if not isinstance(age, int) or age < 0 or age > 150:
            raise ValidationError(field_name="age", value=age, reason="Возраст должен быть числом от 0 до 150")
        
        patient = Patient(id=self._get_patioent_id(), name=name, age=age)
        self.patient_repo.add(patient)
        return patient

    def admit_to_ward(self, patient_id: int, ward_id: int) -> None:
        patient = self.patient_repo.get_by_id(patient_id)
        if not patient:
            # Используем новое исключение с методами get_suggestion() и get_error_info()
            raise PatientNotFoundException(patient_id=patient_id)

        ward = self.ward_repo.get_by_id(ward_id)
        if not ward:
            raise WardNotFoundException(ward_id=ward_id)

        if patient.ward_id is not None:
            raise AlreadyHospitalizedException(patient_id)

        if len(ward.current_patients) >= ward.capacity:
            raise WardFullException(
                ward_number=ward.number,
                capacity=ward.capacity,
                current_count=len(ward.current_patients)
            )

        ward.current_patients.append(patient.id)
        patient.ward_id = ward.id

        self.ward_repo.update(ward)
        self.patient_repo.update(patient)

    def discharge_patient(self, patient_id: int) -> None:
        """выписать пациента"""
        patient = self.patient_repo.get_by_id(patient_id)
        if not patient:
            raise PatientNotFoundException(patient_id)

        # был ли пациент госпитализирован
        if patient.ward_id is None:
            raise PatientNotHospitalizedException(patient_id)

        # Удаляем пациента из палаты
        ward = self.ward_repo.get_by_id(patient.ward_id)
        if ward:
            if patient_id in ward.current_patients:
                ward.current_patients.remove(patient_id)
            self.ward_repo.update(ward)

        # Обнуляем связь у пациента
        patient.ward_id = None
        self.patient_repo.update(patient)


class AppointmentService:
    """Логика работы с приёмами и диагнозами"""

    def __init__(self, appointment_repo: Repository, patient_repo: Repository, doctor_repo: Repository) -> None:
        self.appointment_repo = appointment_repo
        self.patient_repo = patient_repo
        self.doctor_repo = doctor_repo
        self._next_appointment_id = 1

    def _generate_appointment_id(self) -> int:
        appointment_id = self._next_appointment_id
        self._next_appointment_id += 1
        return appointment_id

    def schedule_appointment(self, patient_id: int, doctor_id: int, dt: datetime) -> Appointment:
        patient = self.patient_repo.get_by_id(patient_id)
        if not patient:
            raise PatientNotFoundException(patient_id=patient_id)

        doctor = self.doctor_repo.get_by_id(doctor_id)
        if not doctor:
            raise DoctorNotFoundException(doctor_id=doctor_id)

        if dt < datetime.now():
            raise InvalidScheduleException(
                reason="Нельзя записать на прошедшее время",
                datetime_value=dt.isoformat()
            )

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

        if patient.ward_id is None:
            raise PatientNotHospitalizedException(patient.id)

        appointment.diagnosis = diagnosis
        appointment.prescription = prescription

        patient = self.patient_repo.get_by_id(appointment.patient_id)
        if patient:
            patient.diagnosis_history.append(f"{diagnosis} ({appointment.date_time.date()})")
            self.patient_repo.update(patient)

        self.appointment_repo.update(appointment)


class DoctorService:
    """Логика работы с врачами"""

    def __init__(self, doctor_repo: Repository) -> None:
        self.doctor_repo = doctor_repo
        self._next_doctor_id = 1
        self._add_default_values()

    def _add_default_values(self):
        self.add_doctor("Доктор Хаус", "Терапевт")

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
            raise ValidationError(field_name="name", value=name,
                                  reason="Имя врача должно быть непустой строкой")
        if specialization is not None and (not isinstance(specialization, str) or not specialization.strip()):
            raise ValidationError(field_name="specialization", value=specialization,
                                  reason="Специализация должна быть заполнена")

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
        self._add_default_values()

    def _add_default_values(self):
        self.add_ward(101, 2)

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

    def get_ward(self, ward_id: int) -> Ward | None:
        ward = self.ward_repo.get_by_id(ward_id)
        if ward:
            return ward
        return None

    def list_wards(self) -> List[Ward]:
        return self.ward_repo.get_all()

