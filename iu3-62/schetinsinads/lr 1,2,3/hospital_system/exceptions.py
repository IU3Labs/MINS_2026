class HospitalSystemError(Exception):
    """Базовое исключение домена."""


class ValidationError(HospitalSystemError):
    """Ошибка валидации входных данных."""


class NotFoundError(HospitalSystemError):
    """Сущность не найдена."""


class StorageError(HospitalSystemError):
    """Ошибка хранения/чтения данных."""


class BusinessRuleError(HospitalSystemError):
    """Нарушение бизнес-правил."""