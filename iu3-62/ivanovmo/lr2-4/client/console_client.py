import grpc
import uuid
import sys
import os

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
import core_pb2
import core_pb2_grpc

def generate_trace_id():
    return str(uuid.uuid4())[:8]

def safe_rpc_call(stub_method, request, metadata, error_msg="Service error"):
    """Выполняет gRPC-вызов с обработкой исключений и возвращает ответ или None."""
    try:
        return stub_method(request, metadata=metadata)
    except grpc.RpcError as e:
        if e.code() == grpc.StatusCode.NOT_FOUND:
            print("❌ Заказ не найден.")
        elif e.code() == grpc.StatusCode.UNAVAILABLE:
            print("❌ Сервис временно недоступен. Попробуйте позже.")
        else:
            print(f"❌ {error_msg} (код: {e.code()})")
        return None
    except Exception as e:
        print(f"❌ Неожиданная ошибка: {e}")
        return None

def run():
    channel = grpc.insecure_channel('localhost:50051')
    stub = core_pb2_grpc.CoreServiceStub(channel)
    trace_id = generate_trace_id()
    metadata = (('x-trace-id', trace_id),)
    print(f"=== Транспортная компания (Trace ID: {trace_id}) ===")

    while True:
        print("\n1. Создать заказ\n2. Статус заказа\n3. Назначить транспорт\n4. Обновить статус доставки\n5. Детали заказа\n0. Выход")
        choice = input("Выберите: ")

        if choice == '1':
            cust = input("Имя клиента: ")
            pickup = input("Адрес подачи: ")
            delivery = input("Адрес доставки: ")
            try:
                weight = float(input("Вес (кг): "))
                distance = float(input("Расстояние (км): "))
            except ValueError:
                print("❌ Некорректное число.")
                continue
            zone = input("Зона (city/suburb/rural): ")

            resp = safe_rpc_call(
                stub.CreateOrder,
                core_pb2.CreateOrderRequest(
                    customer_name=cust,
                    pickup_address=pickup,
                    delivery_address=delivery,
                    weight_kg=weight,
                    distance_km=distance,
                    zone=zone
                ),
                metadata,
                "Не удалось создать заказ"
            )
            if resp:
                if resp.status == "error":
                    print(f"❌ Ошибка: {resp.message}")
                else:
                    print(f"✅ Заказ {resp.order_id} создан. Статус: {resp.status}. Стоимость: {resp.total_cost} руб.")

        elif choice == '2':
            oid = input("ID заказа: ")
            resp = safe_rpc_call(stub.GetOrderStatus, core_pb2.GetOrderStatusRequest(order_id=oid), metadata)
            if resp:
                print(f"📦 Статус: {resp.status}, Транспорт: {resp.assigned_vehicle}")

        elif choice == '3':
            oid = input("ID заказа: ")
            vtype = input("Тип ТС (van_small/van_large/truck): ")
            resp = safe_rpc_call(stub.AssignTransport, core_pb2.AssignTransportRequest(order_id=oid, vehicle_type_id=vtype), metadata)
            if resp:
                if resp.success:
                    print(f"✅ {resp.message}. ID ТС: {resp.vehicle_id}")
                else:
                    print(f"❌ {resp.message}")

        elif choice == '4':
            oid = input("ID заказа: ")
            new_status = input("Новый статус (in_transit/delivered): ")
            resp = safe_rpc_call(stub.UpdateDeliveryStatus, core_pb2.UpdateStatusRequest(order_id=oid, new_status=new_status), metadata)
            if resp:
                if resp.success:
                    print(f"✅ {resp.message}")
                else:
                    print(f"❌ {resp.message}")

        elif choice == '5':
            oid = input("ID заказа: ")
            resp = safe_rpc_call(stub.GetOrderDetails, core_pb2.GetOrderDetailsRequest(order_id=oid), metadata)
            if resp:
                print(f"👤 Клиент: {resp.customer_name}")
                print(f"📍 Подача: {resp.pickup_address}")
                print(f"🏁 Доставка: {resp.delivery_address}")
                print(f"⚖ Вес: {resp.weight_kg} кг, 📏 Расстояние: {resp.distance_km} км, 🗺 Зона: {resp.zone}")
                print(f"💰 Стоимость: {resp.total_cost} руб., 📌 Статус: {resp.status}")

        elif choice == '0':
            print("До свидания!")
            break
        else:
            print("❌ Неверный пункт меню.")

if __name__ == '__main__':
    run()