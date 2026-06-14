from __future__ import annotations

import grpc

from lab4_microservices.common.grpc_utils import metadata_with_trace
from lab4_microservices.common.trace import new_trace_id
from lab4_microservices.generated import core_service_pb2, core_service_pb2_grpc
from lab4_microservices.generated import reference_service_pb2, reference_service_pb2_grpc

DEFAULT_CORE_TARGET = "127.0.0.1:50051"
DEFAULT_REFERENCE_TARGET = "127.0.0.1:50052"


class ConsoleClient:
    def __init__(self, core_target: str = DEFAULT_CORE_TARGET, reference_target: str = DEFAULT_REFERENCE_TARGET):
        self._core_channel = grpc.insecure_channel(core_target)
        self._reference_channel = grpc.insecure_channel(reference_target)
        self._core = core_service_pb2_grpc.CoreServiceStub(self._core_channel)
        self._reference = reference_service_pb2_grpc.ReferenceServiceStub(self._reference_channel)
        self._actions = {
            "1": self._cmd_add_product,
            "2": self._cmd_list_products,
            "3": self._cmd_add_stock,
            "4": self._cmd_remove_stock,
            "5": self._cmd_move_stock,
            "6": self._cmd_list_zone,
            "7": self._cmd_summary,
            "8": self._cmd_list_kinds,
        }

    def run(self) -> None:
        while True:
            self._print_menu()
            command = input("Выберите действие: ").strip()
            if command == "0":
                print("Выход из программы.")
                return

            action = self._actions.get(command)
            if action is None:
                print("Неизвестная команда.")
                continue

            trace_id = new_trace_id()
            print(f"[TRACE ID] {trace_id}")
            try:
                action(trace_id)
            except grpc.RpcError as exc:
                print(f"[ОШИБКА gRPC] {exc.code().name}: {exc.details()}")
            except Exception as exc:
                print(f"[ОШИБКА] {exc}")

    def _print_menu(self) -> None:
        print("\n=== ЛР4: микросервисная складская система ===")
        print("1) Добавить товар в каталог (Reference Service)")
        print("2) Показать каталог товаров (Reference Service)")
        print("3) Добавить остаток в зону (Core Service)")
        print("4) Списать остаток из зоны (Core Service)")
        print("5) Переместить товар между зонами (Core Service)")
        print("6) Показать содержимое зоны (Core Service)")
        print("7) Показать статистику (Core Service)")
        print("8) Показать доступные типы товаров (Reference Service)")
        print("0) Выход")

    def _cmd_add_product(self, trace_id: str) -> None:
        kind_response = self._reference.ListKinds(
            reference_service_pb2.ListKindsRequest(), metadata=metadata_with_trace(trace_id)
        )
        print("Доступные типы:")
        for kind in kind_response.kinds:
            print(f"- {kind.code} ({kind.title})")

        response = self._reference.AddProduct(
            reference_service_pb2.AddProductRequest(
                product_id=self._ask_text("ID товара"),
                name=self._ask_text("Название"),
                unit_price=self._ask_float("Цена за единицу"),
                kind_codes=self._ask_csv("Типы товара"),
            ),
            metadata=metadata_with_trace(trace_id),
        )
        print(response.message)

    def _cmd_list_products(self, trace_id: str) -> None:
        sort_by = input("Код сортировки [id/name/price/tags]: ").strip() or "id"
        response = self._reference.ListProducts(
            reference_service_pb2.ListProductsRequest(sort_by=sort_by),
            metadata=metadata_with_trace(trace_id),
        )
        if not response.success:
            print(response.message)
            return
        if not response.products:
            print("Каталог пуст.")
            return

        print("\nКаталог товаров:")
        for product in response.products:
            print(
                f"- ID={product.product_id}; название={product.name}; "
                f"цена={product.unit_price:.2f}; теги={', '.join(product.tags)}"
            )

    def _cmd_add_stock(self, trace_id: str) -> None:
        response = self._core.AddStock(
            core_service_pb2.AddStockRequest(
                zone=self._ask_text("Зона"),
                product_id=self._ask_text("ID товара"),
                qty=self._ask_int("Количество"),
            ),
            metadata=metadata_with_trace(trace_id),
        )
        print(response.message)

    def _cmd_remove_stock(self, trace_id: str) -> None:
        response = self._core.RemoveStock(
            core_service_pb2.RemoveStockRequest(
                zone=self._ask_text("Зона"),
                product_id=self._ask_text("ID товара"),
                qty=self._ask_int("Количество"),
            ),
            metadata=metadata_with_trace(trace_id),
        )
        print(response.message)

    def _cmd_move_stock(self, trace_id: str) -> None:
        response = self._core.MoveStock(
            core_service_pb2.MoveStockRequest(
                from_zone=self._ask_text("Из зоны"),
                to_zone=self._ask_text("В зону"),
                product_id=self._ask_text("ID товара"),
                qty=self._ask_int("Количество"),
            ),
            metadata=metadata_with_trace(trace_id),
        )
        print(response.message)

    def _cmd_list_zone(self, trace_id: str) -> None:
        response = self._core.ListZone(
            core_service_pb2.ListZoneRequest(zone=self._ask_text("Зона")),
            metadata=metadata_with_trace(trace_id),
        )
        if not response.success:
            print(response.message)
            return
        if not response.items:
            print("Зона пуста.")
            return

        print("\nСодержимое зоны:")
        for item in response.items:
            print(
                f"- ID={item.product.product_id}; название={item.product.name}; "
                f"количество={item.qty}; теги={', '.join(item.product.tags)}"
            )

    def _cmd_summary(self, trace_id: str) -> None:
        response = self._core.GetSummary(
            core_service_pb2.GetSummaryRequest(),
            metadata=metadata_with_trace(trace_id),
        )
        if not response.success:
            print(response.message)
            return

        summary = response.summary
        print("\nОбщая статистика:")
        print(f"- Всего единиц товара: {summary.total_units}")
        print(f"- Всего складских позиций: {summary.total_positions}")
        print(f"- Общая стоимость товаров: {summary.total_value:.2f}")

        print("\nПо зонам:")
        for zone in summary.by_zone:
            print(
                f"- {zone.zone}: единиц={zone.units}; позиций={zone.positions}; стоимость={zone.value:.2f}"
            )

        print("\nПо тегам:")
        if not summary.by_tag:
            print("- Нет данных")
        else:
            for tag in summary.by_tag:
                print(f"- {tag.tag}: {tag.units}")

        print("\nПо товарам:")
        if not summary.by_product:
            print("- Нет данных")
        else:
            for product in summary.by_product:
                print(
                    f"- ID={product.product_id}; название={product.name}; единиц={product.units}; стоимость={product.value:.2f}"
                )

    def _cmd_list_kinds(self, trace_id: str) -> None:
        response = self._reference.ListKinds(
            reference_service_pb2.ListKindsRequest(), metadata=metadata_with_trace(trace_id)
        )
        print("Доступные типы товаров:")
        for kind in response.kinds:
            print(f"- {kind.code} ({kind.title})")

    @staticmethod
    def _ask_text(caption: str) -> str:
        value = input(f"{caption}: ").strip()
        if not value:
            raise ValueError(f"Поле '{caption}' не может быть пустым")
        return value

    @staticmethod
    def _ask_int(caption: str) -> int:
        return int(input(f"{caption}: ").strip())

    @staticmethod
    def _ask_float(caption: str) -> float:
        return float(input(f"{caption}: ").strip().replace(",", "."))

    @staticmethod
    def _ask_csv(caption: str) -> list[str]:
        return [item.strip() for item in input(f"{caption}: ").strip().split(",") if item.strip()]


def main() -> None:
    ConsoleClient().run()


if __name__ == "__main__":
    main()
