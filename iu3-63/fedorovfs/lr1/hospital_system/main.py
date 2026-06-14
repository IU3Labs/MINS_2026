from repositories import InMemoryRepository
from services import PatientService, DoctorService, WardService, AppointmentService
from ui import HospitalSystemUI


def main() -> None:

    # репозитории
    patient_repo = InMemoryRepository()
    doctor_repo = InMemoryRepository()
    ward_repo = InMemoryRepository()
    appointment_repo = InMemoryRepository()

    # сервисы
    patient_service = PatientService(patient_repo, ward_repo)
    doctor_service = DoctorService(doctor_repo)
    ward_service = WardService(ward_repo)
    appointment_service = AppointmentService(
        appointment_repo,
        patient_repo,
        doctor_repo,
    )

    # начальные данные
    # doctor_service.add_doctor("Доктор Хаус", "Терапевт")
    # ward_service.add_ward(101, 2)

    # UI получает готовые сервисы (Dependency Injection)
    app = HospitalSystemUI(
        patient_service=patient_service,
        doctor_service=doctor_service,
        ward_service=ward_service,
        appointment_service=appointment_service,
    )

    try:
        app.run()
    except Exception as e:
        print(f"Ошибка системы: {e}")


if __name__ == "__main__":
    main()

