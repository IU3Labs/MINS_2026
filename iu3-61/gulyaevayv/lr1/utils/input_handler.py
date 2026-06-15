from datetime import datetime
from exceptions.pharmacy_exceptions import InputException


class InputHandler:

    @staticmethod
    def input_string(prompt):

        value = input(prompt).strip()

        if not value:
            raise InputException("Пустая строка")

        return value

    @staticmethod
    def input_float(prompt):

        try:
            value = float(input(prompt))

            if value < 0:
                raise InputException("Число не может быть отрицательным")

            return value

        except ValueError:
            raise InputException("Введите корректное число")

    @staticmethod
    def input_int(prompt):

        try:
            value = int(input(prompt))

            if value < 0:
                raise InputException("Число не может быть отрицательным")

            return value

        except ValueError:
            raise InputException("Введите целое число")

    @staticmethod
    def input_date(prompt):

        value = input(prompt).strip()

        try:
            return datetime.strptime(value, "%Y-%m-%d").date()

        except ValueError:
            raise InputException("Некорректная дата (YYYY-MM-DD)")

    @staticmethod
    def input_bool(prompt):

        value = input(prompt).lower()

        if value in ["y", "yes"]:
            return True
        elif value in ["n", "no"]:
            return False

        raise InputException("Введите y/n")
