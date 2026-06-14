from notifications import EmailMockObserver, EventManager, FileAndConsoleLogObserver
from repositories import InMemoryRepository
from services import AppointmentService, PatientService
from ui import HospitalSystemUI
from grpc_client import ReferenceGrpcClient


def main() -> None:
    event_manager = EventManager()
    event_manager.subscribe(FileAndConsoleLogObserver("hospital_notifications.log.txt"))
    event_manager.subscribe(EmailMockObserver())

    patient_repo = InMemoryRepository()
    appointment_repo = InMemoryRepository()

    # Подключаем gRPC клиент
    reference_client = ReferenceGrpcClient()

    patient_service = PatientService(patient_repo, reference_client, event_manager)
    appointment_service = AppointmentService(appointment_repo, patient_repo, reference_client, event_manager)

    app = HospitalSystemUI(
        patient_service=patient_service,
        doctor_service=None,  # Сервис А больше не управляет докторами
        ward_service=None,    # Сервис А больше не управляет справочниками
        appointment_service=appointment_service,
    )

    try:
        app.run()
    except Exception as e:
        print(f"Ошибка системы: {e}")

if __name__ == "__main__":
    main()