from typing import Optional, Any, Dict


class HospitalException(Exception):
    """Базовое исключение для системы больницы"""
    
    def __init__(self, message: str, error_code: int = 1000, details: Optional[Dict[str, Any]] = None):
        super().__init__(message)
        self.message = message
        self.error_code = error_code
        self.details = details or {}
    
    def get_error_info(self) -> Dict[str, Any]:
        """Возвращает полную информацию об ошибке"""
        return {
            "error_code": self.error_code,
            "message": self.message,
            "details": self.details,
            "type": self.__class__.__name__
        }
    
    def get_user_message(self) -> str:
        """Формирует понятное сообщение для пользователя"""
        base_msg = f"[Ошибка {self.error_code}] {self.message}"
        if self.details:
            extra_info = ", ".join(f"{k}: {v}" for k, v in self.details.items() if k != 'operation')
            if extra_info:
                return f"{base_msg}. Детали: {extra_info}"
        return base_msg
    
    def __str__(self) -> str:
        base = f"[Ошибка {self.error_code}] {self.message}"
        if self.details:
            base += f" | Детали: {self.details}"
        return base


class DataAccessException(HospitalException):
    """Исключение доступа к данным (для репозиториев)"""
    
    def __init__(self, message: str, entity_type: str, entity_id: Optional[int] = None):
        details = {"entity_type": entity_type}
        if entity_id is not None:
            details["entity_id"] = entity_id
        super().__init__(message, error_code=2000, details=details)
    
    def get_entity_info(self) -> str:
        """Возвращает информацию о сущности"""
        info = f"Тип: {self.details['entity_type']}"
        if "entity_id" in self.details:
            info += f", ID: {self.details['entity_id']}"
        return info


class NotFoundException(DataAccessException):
    """Сущность не найдена"""
    
    def __init__(self, entity_type: str, entity_id: int):
        super().__init__(
            f"{entity_type} с ID {entity_id} не найден",
            entity_type=entity_type,
            entity_id=entity_id
        )
        self.error_code = 2001


class PatientNotFoundException(NotFoundException):
    """Пациент не найден"""
    
    def __init__(self, patient_id: int):
        super().__init__(entity_type="Пациент", entity_id=patient_id)
        self.error_code = 2002
        self.patient_id = patient_id
    
    def get_suggestion(self) -> str:
        """Возвращает рекомендацию"""
        return f"Проверьте существование пациента с ID={self.patient_id} или зарегистрируйте нового"


class DoctorNotFoundException(NotFoundException):
    """Врач не найден"""
    
    def __init__(self, doctor_id: int):
        super().__init__(entity_type="Врач", entity_id=doctor_id)
        self.error_code = 2003
        self.doctor_id = doctor_id


class WardNotFoundException(NotFoundException):
    """Палата не найдена"""
    
    def __init__(self, ward_id: int):
        super().__init__(entity_type="Палата", entity_id=ward_id)
        self.error_code = 2004
        self.ward_id = ward_id


class AppointmentNotFoundException(NotFoundException):
    """Приём не найден"""
    
    def __init__(self, appointment_id: int):
        super().__init__(entity_type="Приём", entity_id=appointment_id)
        self.error_code = 2005


class BusinessLogicException(HospitalException):
    """Бизнес-логика нарушена"""
    
    def __init__(self, message: str, operation: str, details: Optional[Dict[str, Any]] = None):
        ext_details = details or {}
        ext_details["operation"] = operation
        super().__init__(message, error_code=3000, details=ext_details)
    
    def get_operation_info(self) -> str:
        """Возвращает информацию об операции"""
        return f"Операция: {self.details.get('operation', 'неизвестно')}"


class CapacityExceededException(BusinessLogicException):
    """Превышение вместимости"""
    
    def __init__(self, entity_type: str, current: int, capacity: int):
        details = {
            "entity_type": entity_type,
            "current_count": current,
            "max_capacity": capacity
        }
        super().__init__(
            f"Превышена вместимость {entity_type}: {current}/{capacity}",
            operation="размещение",
            details=details
        )
        self.error_code = 3001


class WardFullException(CapacityExceededException):
    """В палате нет свободных мест"""
    
    def __init__(self, ward_number: int, capacity: int, current_count: int):
        super().__init__(
            entity_type=f"палаты №{ward_number}",
            current=current_count,
            capacity=capacity
        )
        self.error_code = 3002
        self.ward_number = ward_number
    
    def get_alternative_action(self) -> str:
        """Возвращает альтернативное действие"""
        return f"Найдите другую палату или увеличьте вместимость палаты №{self.ward_number}"


class ScheduleConflictException(BusinessLogicException):
    """Конфликт расписания"""
    
    def __init__(self, doctor_id: int, datetime_str: str):
        details = {"doctor_id": doctor_id, "requested_time": datetime_str}
        super().__init__(
            f"У врача ID={doctor_id} уже есть запись на {datetime_str}",
            operation="планирование приёма",
            details=details
        )
        self.error_code = 3003


class InvalidScheduleException(BusinessLogicException):
    """Некорректное время приёма"""
    
    def __init__(self, reason: str, datetime_value: Optional[str] = None):
        details = {}
        if datetime_value:
            details["invalid_datetime"] = datetime_value
        super().__init__(
            f"Некорректное время: {reason}",
            operation="планирование приёма",
            details=details
        )
        self.error_code = 3004


class ValidationError(HospitalException):
    """Ошибка валидации данных"""
    
    def __init__(self, field_name: str, value: Any, reason: str):
        details = {
            "field": field_name,
            "invalid_value": str(value),
            "reason": reason
        }
        super().__init__(
            f"Некорректное значение поля '{field_name}': {reason}",
            error_code=4000,
            details=details
        )
        self.field_name = field_name
        self.invalid_value = value
    
    def get_fix_suggestion(self) -> str:
        """Возвращает рекомендацию по исправлению"""
        return f"Проверьте поле '{self.field_name}'. Текущее значение: {self.invalid_value}"


class PrescriptionException(HospitalException):
    """Ошибка при выписке рецепта"""
    
    def __init__(self, reason: str, prescription_text: Optional[str] = None):
        details = {}
        if prescription_text:
            details["prescription"] = prescription_text
        super().__init__(
            f"Ошибка выписки рецепта: {reason}",
            error_code=5000,
            details=details
        )
        self.reason = reason
    
    def log_prescription_attempt(self) -> str:
        """Логирует попытку выписки"""
        return f"[LOG] Попытка выписки failed: {self.reason}"


class AlreadyHospitalizedException(BusinessLogicException):
    """Пациент уже находится в палате"""
    def __init__(self, patient_id: int):
        super().__init__(
            f"Пациент с ID {patient_id} уже госпитализирован",
            "Сначала выпишите пациента из текущей палаты."
        )


class PatientNotHospitalizedException(BusinessLogicException):
    """Пациент не госпитализирован (выписан или не лежал)"""
    def __init__(self, patient_id: int):
        super().__init__(
            f"Пациент с ID {patient_id} не находится в стационаре",
            "Нельзя выполнить операцию для не госпитализированного пациента."
        )


class DuplicateWardNumberException(BusinessLogicException):
    """Палата с таким номером уже существует"""
    def __init__(self, number: int):
        super().__init__(
            f"Палата с номером {number} уже существует",
            "Используйте уникальный номер палаты."
        )


class InvalidCapacityException(BusinessLogicException):
    """Некорректная вместимость"""
    def __init__(self, capacity: int):
        super().__init__(
            f"Вместимость {capacity} недопустима",
            "Вместимость должна быть положительным числом (> 0)."
        )

