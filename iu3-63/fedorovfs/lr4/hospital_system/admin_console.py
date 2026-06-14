import grpc
import uuid
import hospital_pb2
import hospital_pb2_grpc


def _get_metadata():
    """Trace ID для отслеживания запросов из админки"""
    trace_id = str(uuid.uuid4())
    return (('x-trace-id', trace_id),)


def print_menu():
    print("\nПАНЕЛЬ АДМИНИСТРАТОРА")
    print("1. Добавить врача (gRPC -> Service B)")
    print("2. Показать всех врачей (gRPC -> Service B)")
    print("0. Выход")


def run_admin_console():
    channel = grpc.insecure_channel('localhost:50051')
    stub = hospital_pb2_grpc.ReferenceServiceStub(channel)

    while True:
        print_menu()
        choice = input("Выберите пункт: ").strip()

        if choice == "0":
            print("Закрытие панели администратора.")
            break

        elif choice == "1":
            name = input("Имя врача: ").strip()
            spec = input("Специализация: ").strip()

            request = hospital_pb2.AddDoctorRequest(name=name, specialization=spec)
            try:
                response = stub.AddDoctor(request, metadata=_get_metadata())
                print(f"Врач добавлен! Назначен ID: {response.id}")
            except grpc.RpcError as e:
                print(f"Ошибка соединения с сервером справочников: {e.details()}")

        elif choice == "2":
            try:
                response = stub.ListDoctors(hospital_pb2.Empty(), metadata=_get_metadata())
                if not response.doctors:
                    print("Список врачей пуст.")
                else:
                    print("\nСПИСОК ВРАЧЕЙ")
                    for doc in response.doctors:
                        print(f"ID: {doc.id} | Имя: {doc.name} | Спец: {doc.specialization}")
            except grpc.RpcError as e:
                print(f"Ошибка соединения с сервером справочников: {e.details()}")
        else:
            print("Неверный пункт меню.")


if __name__ == "__main__":
    run_admin_console()