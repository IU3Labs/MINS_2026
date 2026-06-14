from notifications import EmailMockObserver, EventManager, FileAndConsoleLogObserver
from repositories import InMemoryRepository
from services import AppointmentService, DoctorService, PatientService, WardService
from ui import HospitalSystemUI


def main() -> None:
    event_manager = EventManager()
    event_manager.subscribe(FileAndConsoleLogObserver("hospital_notifications.log.txt"))
    event_manager.subscribe(EmailMockObserver())

    patient_repo = InMemoryRepository()
    doctor_repo = InMemoryRepository()
    ward_repo = InMemoryRepository()
    appointment_repo = InMemoryRepository()



    patient_service = PatientService(patient_repo, ward_repo, event_manager)
    doctor_service = DoctorService(doctor_repo)
    ward_service = WardService(ward_repo)
    appointment_service = AppointmentService(
        appointment_repo,
        patient_repo,
        doctor_repo,
        event_manager,
    )

    doctor_service.add_doctor("Доктор Хаус", "Терапевт")
    ward_service.add_ward(101, 2)

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
