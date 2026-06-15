from datetime import date


class FridayDiscountCalculator:

    def calculate(self, medicines, medicine_name, quantity):
        result = ""

        try:
            if quantity <= 0:
                return "Количество должно быть больше 0"

            found = None

            for medicine in medicines:
                try:
                    if medicine.name.lower() == medicine_name.lower():
                        found = medicine
                        break
                except Exception:
                    pass

            if found is None:
                return "Лекарство для расчета акции не найдено"

            total = found.price * quantity
            discount = 0
            final_total = total
            promo_message = "Акция не применена"

            if date.today().weekday() == 4:
                if "вертекс" in found.manufacturer.lower():
                    discount = total * 0.15
                    final_total = total - discount
                    promo_message = "Применена пятничная скидка 15% на производителя Вертекс"
                else:
                    promo_message = "Сегодня пятница, но производитель не участвует в акции"
            else:
                promo_message = "Акция действует только по пятницам"

            if found.quantity < quantity:
                promo_message = promo_message + ". На складе меньше товара, это только примерный расчет"

            result = (
                "Быстрый расчет акции без сохранения продажи\n"
                f"Лекарство: {found.name}\n"
                f"Производитель: {found.manufacturer}\n"
                f"Количество: {quantity}\n"
                f"Цена без скидки: {round(total, 2)}\n"
                f"Скидка: {round(discount, 2)}\n"
                f"Итого: {round(final_total, 2)}\n"
                f"{promo_message}"
            )
        except Exception:
            result = "Не удалось рассчитать акцию"

        return result
