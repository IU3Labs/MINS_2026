import json
from datetime import datetime
from pathlib import Path

from warehouse_app.exceptions import UserInputError


class QuickOrderGodClass:
    """
    ЛР3: намеренно плохой модуль для демонстрации антипаттерна God Class.

    Этот класс специально нарушает SRP:
    - сам читает пользовательский ввод,
    - сам парсит строки,
    - сам работает с JSON-файлом напрямую,
    - сам считает скидки и наценки,
    - сам форматирует и печатает отчёт.

    Такой код НЕ является образцом качества и добавлен только
    как локализованный технический долг для лабораторной работы.
    """

    def __init__(self, data_file: str | Path):
        self.data_file = Path(data_file)
        self.history: list[dict] = []

        self.friday_food_discount_percent = 15
        self.bulk_discount_percent = 5
        self.bulk_discount_threshold = 10
        self.vip_discount_percent = 10
        self.small_order_fee = 150
        self.express_delivery_fee = 300
        self.packaging_fee = 25

    def run_console_flow(self) -> None:
        print("\n=== Быстрый расчёт примерной стоимости заказа ===")
        print("Формат ввода позиций: product_id:qty,product_id:qty")
        print("Пример: 1:2,10:3")
        print("Расчёт ничего не сохраняет в базу и не меняет складские остатки.")

        raw_items = input("Позиции заказа: ").strip()
        raw_delivery = input("Доставка [обычная/срочная]: ").strip().lower()
        raw_customer = input("Тип клиента [обычный/vip]: ").strip().lower()
        raw_zone = input("Зона для ориентировочной проверки [A/B/C, можно пусто]: ").strip().upper()
        raw_friday = input("Считать, что заказ оформляется в пятницу? [y/N]: ").strip().lower()

        report = self._calculate_everything_in_one_place(
            raw_items=raw_items,
            raw_delivery=raw_delivery,
            raw_customer=raw_customer,
            raw_zone=raw_zone,
            raw_friday=raw_friday,
        )
        self._print_report(report)

    def _calculate_everything_in_one_place(
        self,
        raw_items: str,
        raw_delivery: str,
        raw_customer: str,
        raw_zone: str,
        raw_friday: str,
    ) -> dict:
        if not raw_items:
            raise UserInputError("Нужно указать хотя бы одну позицию заказа")

        raw_payload = json.loads(self.data_file.read_text(encoding="utf-8"))
        catalog = raw_payload.get("catalog", {})
        zones = raw_payload.get("zones", {})

        parsed_items: list[tuple[str, int]] = []
        for raw_part in raw_items.split(","):
            part = raw_part.strip()
            if not part:
                continue
            if ":" not in part:
                raise UserInputError(
                    "Каждая позиция должна быть в формате product_id:qty"
                )
            product_id, qty_text = part.split(":", 1)
            product_id = product_id.strip()
            qty_text = qty_text.strip()
            qty = int(qty_text)
            if qty <= 0:
                raise UserInputError("Количество по позиции должно быть больше 0")
            parsed_items.append((product_id, qty))

        if not parsed_items:
            raise UserInputError("После разбора заказа не осталось ни одной корректной позиции")

        friday_mode = raw_friday in {"y", "yes", "да", "д", "1"}
        if not friday_mode:
            friday_mode = datetime.now().weekday() == 4

        line_reports: list[dict] = []
        warnings: list[str] = []
        subtotal = 0.0
        total_discount = 0.0
        total_qty = 0

        for product_id, qty in parsed_items:
            raw_product = catalog.get(product_id)
            if raw_product is None:
                warnings.append(f"Товар с ID={product_id} не найден в каталоге и пропущен")
                continue

            name = str(raw_product.get("name", "Без названия"))
            price = float(raw_product.get("unit_price", 0))
            tags = [str(tag) for tag in raw_product.get("tags", [])]

            line_total = price * qty
            line_discount = 0.0
            reasons: list[str] = []

            if friday_mode and "food" in tags:
                friday_discount = line_total * self.friday_food_discount_percent / 100
                line_discount += friday_discount
                reasons.append(
                    f"пятничная скидка {self.friday_food_discount_percent}% на food"
                )

            if qty >= self.bulk_discount_threshold:
                bulk_discount = line_total * self.bulk_discount_percent / 100
                line_discount += bulk_discount
                reasons.append(
                    f"скидка {self.bulk_discount_percent}% за объём от {self.bulk_discount_threshold}"
                )

            if raw_zone:
                if raw_zone not in zones:
                    warnings.append(
                        f"Зона {raw_zone} отсутствует в файле склада, проверка зоны не выполнена"
                    )
                else:
                    existing_qty = zones[raw_zone].get(product_id)
                    if existing_qty is not None:
                        reasons.append(
                            f"в зоне {raw_zone} уже есть {existing_qty} ед. этого товара"
                        )

            subtotal += line_total
            total_discount += line_discount
            total_qty += qty
            line_reports.append(
                {
                    "product_id": product_id,
                    "name": name,
                    "qty": qty,
                    "price": price,
                    "line_total": round(line_total, 2),
                    "line_discount": round(line_discount, 2),
                    "line_final": round(line_total - line_discount, 2),
                    "reasons": reasons,
                }
            )

        if not line_reports:
            raise UserInputError("Невозможно выполнить расчёт: все позиции были отброшены")

        delivery_fee = 0.0
        if raw_delivery in {"срочная", "express", "exp", "1"}:
            delivery_fee = float(self.express_delivery_fee)
        elif subtotal < 1000:
            delivery_fee = float(self.small_order_fee)

        customer_discount = 0.0
        if raw_customer in {"vip", "2"}:
            customer_discount = (subtotal - total_discount) * self.vip_discount_percent / 100

        packaging_fee = float(self.packaging_fee) if total_qty > 0 else 0.0
        total = subtotal - total_discount - customer_discount + delivery_fee + packaging_fee

        report = {
            "created_at": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "delivery": raw_delivery or "обычная",
            "customer": raw_customer or "обычный",
            "zone": raw_zone or "не указана",
            "friday_mode": friday_mode,
            "subtotal": round(subtotal, 2),
            "line_discount": round(total_discount, 2),
            "customer_discount": round(customer_discount, 2),
            "delivery_fee": round(delivery_fee, 2),
            "packaging_fee": round(packaging_fee, 2),
            "total": round(total, 2),
            "positions": line_reports,
            "warnings": warnings,
        }
        self.history.append(report)
        return report

    def _print_report(self, report: dict) -> None:
        print("\n--- Быстрый расчёт заказа ---")
        print(f"Дата расчёта: {report['created_at']}")
        print(f"Тип доставки: {report['delivery']}")
        print(f"Тип клиента: {report['customer']}")
        print(f"Проверяемая зона: {report['zone']}")
        print(f"Режим пятницы: {'да' if report['friday_mode'] else 'нет'}")
        print("\nПозиции:")
        for item in report["positions"]:
            print(
                f"- ID={item['product_id']}; название={item['name']}; qty={item['qty']}; "
                f"цена={item['price']:.2f}; сумма={item['line_total']:.2f}; "
                f"скидка={item['line_discount']:.2f}; итого={item['line_final']:.2f}"
            )
            if item["reasons"]:
                print(f"  причины: {'; '.join(item['reasons'])}")

        print("\nИтоги:")
        print(f"- Подытог: {report['subtotal']:.2f}")
        print(f"- Скидки по позициям: {report['line_discount']:.2f}")
        print(f"- Доп. скидка клиенту: {report['customer_discount']:.2f}")
        print(f"- Стоимость доставки: {report['delivery_fee']:.2f}")
        print(f"- Упаковка: {report['packaging_fee']:.2f}")
        print(f"- Примерная стоимость заказа: {report['total']:.2f}")

        if report["warnings"]:
            print("\nПредупреждения:")
            for warning in report["warnings"]:
                print(f"- {warning}")
