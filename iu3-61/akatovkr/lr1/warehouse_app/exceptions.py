class WarehouseError(Exception):
    """Базовое исключение складской системы."""


class ValidationError(WarehouseError):
    """Ошибки валидации данных."""


class UserInputError(ValidationError):
    """Некорректный пользовательский ввод."""


class EmptyFieldError(UserInputError):
    """Обязательное поле не заполнено."""


class NumericFormatError(UserInputError):
    """Числовое поле содержит неверный формат."""


class UnknownProductKindError(UserInputError):
    """Указан неизвестный тип товара."""


class EntityError(WarehouseError):
    """Ошибки работы с сущностями системы."""


class EntityNotFoundError(EntityError):
    """Сущность не найдена."""


class ProductNotFoundError(EntityNotFoundError):
    """Товар не найден."""


class ZoneNotFoundError(EntityNotFoundError):
    """Зона склада не найдена."""


class DuplicateEntityError(EntityError):
    """Сущность уже существует."""


class DuplicateProductError(DuplicateEntityError):
    """Товар с таким идентификатором уже существует."""


class StockError(WarehouseError):
    """Ошибки учёта остатков."""


class StockLevelError(StockError):
    """Недостаточный остаток товара."""


class CompatibilityError(WarehouseError):
    """Нарушены правила товарного соседства."""


class PersistenceError(WarehouseError):
    """Ошибка загрузки или сохранения данных."""
