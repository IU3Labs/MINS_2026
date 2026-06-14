import json
from datetime import datetime
from pathlib import Path

from warehouse_app.exceptions import (
    EmptyFieldError,
    NumericFormatError,
    PersistenceError,
    ProductNotFoundError,
    UserInputError,
    WarehouseError,
    ZoneNotFoundError,
)


class QuickEstimateError(WarehouseError):
    """ """


class QuickEstimateRuleError(QuickEstimateError):
    """ """


class QuickOrderGodClass:

    def __init__(self, data_file: str | Path):
        self.data_file = Path(data_file)
        self.history: list[dict] = []

        # Намеренно оставлены магические значения внутри God Class.
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

        try:
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
        except (UserInputError, ProductNotFoundError, ZoneNotFoundError) as exc:
            print(f"[ОШИБКА ВВОДА/РАСЧЁТА] {exc}")
            return
        except (PersistenceError, QuickEstimateRuleError) as exc:
            print(f"[ОШИБКА МОДУЛЯ БЫСТРОГО РАСЧЁТА] {exc}")
            return

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
            raise EmptyFieldError("Нужно указать хотя бы одну позицию заказа")

        raw_payload = self._load_raw_payload_or_raise()
        catalog = raw_payload.get("catalog", {})
        zones = raw_payload.get("zones", {})

        if not isinstance(catalog, dict):
            raise PersistenceError("В warehouse_data.json раздел 'catalog' имеет неверный формат")
        if not isinstance(zones, dict):
            raise PersistenceError("В warehouse_data.json раздел 'zones' имеет неверный формат")

        parsed_items = self._parse_items_or_raise(raw_items)
        delivery_kind = self._normalize_delivery_or_raise(raw_delivery)
        customer_kind = self._normalize_customer_or_raise(raw_customer)
        friday_mode = self._normalize_friday_flag(raw_friday)

        line_reports: list[dict] = []
        warnings: list[str] = []
        subtotal = 0.0
        total_discount = 0.0
        total_qty = 0

        zone_snapshot = None
        if raw_zone:
            zone_snapshot = self._resolve_zone_or_raise(raw_zone, zones)

        for product_id, qty in parsed_items:
            raw_product = catalog.get(product_id)
            if raw_product is None:
                raise ProductNotFoundError(f"Товар с ID={product_id} не найден в каталоге")
            if not isinstance(raw_product, dict):
                raise PersistenceError(
                    f"Карточка товара с ID={product_id} имеет неверный формат в warehouse_data.json"
                )

            name = str(raw_product.get("name", "Без названия"))
            try:
                price = float(raw_product.get("unit_price", 0))
            except (TypeError, ValueError) as exc:
                raise PersistenceError(
                    f"У товара с ID={product_id} поле unit_price имеет неверный формат"
                ) from exc

            if price < 0:
                raise QuickEstimateRuleError(
                    f"У товара с ID={product_id} обнаружена отрицательная цена, расчёт невозможен"
                )

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

            if zone_snapshot is not None:
                existing_qty = zone_snapshot.get(product_id)
                if existing_qty is not None:
                    reasons.append(
                        f"в зоне {raw_zone} уже есть {existing_qty} ед. этого товара"
                    )
                else:
                    warnings.append(
                        f"в зоне {raw_zone} товар с ID={product_id} сейчас не найден"
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
            raise QuickEstimateRuleError("Невозможно выполнить расчёт: список позиций пуст после обработки")

        delivery_fee = 0.0
        if delivery_kind == "срочная":
            delivery_fee = float(self.express_delivery_fee)
        elif subtotal < 1000:
            delivery_fee = float(self.small_order_fee)

        customer_discount = 0.0
        if customer_kind == "vip":
            customer_discount = (subtotal - total_discount) * self.vip_discount_percent / 100

        packaging_fee = float(self.packaging_fee) if total_qty > 0 else 0.0
        total = subtotal - total_discount - customer_discount + delivery_fee + packaging_fee

        if total < 0:
            raise QuickEstimateRuleError("Расчёт привёл к отрицательной стоимости заказа")

        report = {
            "created_at": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "delivery": delivery_kind,
            "customer": customer_kind,
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

    def _load_raw_payload_or_raise(self) -> dict:
        try:
            raw_text = self.data_file.read_text(encoding="utf-8")
        except FileNotFoundError as exc:
            raise PersistenceError(
                f"Файл данных склада не найден: {self.data_file}"
            ) from exc
        except OSError as exc:
            raise PersistenceError(
                f"Не удалось прочитать файл данных склада: {self.data_file}"
            ) from exc

        try:
            raw_payload = json.loads(raw_text)
        except json.JSONDecodeError as exc:
            raise PersistenceError(
                f"Файл данных склада повреждён: ошибка JSON в позиции {exc.pos}"
            ) from exc

        if not isinstance(raw_payload, dict):
            raise PersistenceError("Корень warehouse_data.json должен быть JSON-объектом")
        return raw_payload

    def _parse_items_or_raise(self, raw_items: str) -> list[tuple[str, int]]:
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

            if not product_id:
                raise EmptyFieldError("В позиции заказа отсутствует product_id")

            if not qty_text:
                raise EmptyFieldError(
                    f"Для товара с ID={product_id} не указано количество"
                )

            try:
                qty = int(qty_text)
            except ValueError as exc:
                raise NumericFormatError(
                    f"Количество для товара с ID={product_id} должно быть целым числом"
                ) from exc

            if qty <= 0:
                raise UserInputError(
                    f"Количество для товара с ID={product_id} должно быть больше 0"
                )

            parsed_items.append((product_id, qty))

        if not parsed_items:
            raise UserInputError("После разбора заказа не осталось ни одной корректной позиции")

        return parsed_items

    def _normalize_delivery_or_raise(self, raw_delivery: str) -> str:
        if raw_delivery in {"", "обычная", "обыч", "normal", "0"}:
            return "обычная"

        if raw_delivery in {"срочная", "express", "exp", "1"}:
            return "срочная"

        raise UserInputError(
            "Неизвестный тип доставки. Допустимо: обычная или срочная"
        )

    def _normalize_customer_or_raise(self, raw_customer: str) -> str:
        if raw_customer in {"", "обычный", "обыч", "normal", "0"}:
            return "обычный"

        if raw_customer in {"vip", "2"}:
            return "vip"

        raise UserInputError(
            "Неизвестный тип клиента. Допустимо: обычный или vip"
        )

    @staticmethod
    def _normalize_friday_flag(raw_friday: str) -> bool:
        if raw_friday in {"y", "yes", "да", "д", "1"}:
            return True

        if raw_friday in {"n", "no", "нет", "н", "0"}:
            return False

        return datetime.now().weekday() == 4

    def _resolve_zone_or_raise(self, raw_zone: str, zones: dict) -> dict:
        zone_snapshot = zones.get(raw_zone)

        if zone_snapshot is None:
            raise ZoneNotFoundError(
                f"Зона {raw_zone} отсутствует в файле склада, расчёт остановлен"
            )

        if not isinstance(zone_snapshot, dict):
            raise PersistenceError(
                f"Раздел зоны {raw_zone} в warehouse_data.json имеет неверный формат"
            )

        return zone_snapshot

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