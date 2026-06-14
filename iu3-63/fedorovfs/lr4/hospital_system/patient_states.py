from __future__ import annotations

from abc import ABC


class PatientState(ABC):
    """Базовое состояние пациента (State)."""

    key: str
    label: str

    def can_schedule_appointment(self) -> bool:
        # может ли записаться на приём к врачу
        return False

    def can_be_admitted_to_ward(self) -> bool:
        # может быть положен в палату
        return False

    def can_complete_appointment(self) -> bool:
        # можно ли завершить приём у врача
        return False

    def can_be_discharged(self) -> bool:
        # может ли быть выписан
        return False

    def __eq__(self, other: object) -> bool:
        return isinstance(other, PatientState) and self.key == other.key

    def __hash__(self) -> int:
        return hash(self.key)

    def __repr__(self) -> str:
        return f"{self.__class__.__name__}({self.key!r})"


class NotHospitalizedState(PatientState):
    """Не госпитализирован — после регистрации или после амбулаторного приёма."""

    key = "not_hospitalized"
    label = "Не госпитализирован"

    def can_schedule_appointment(self) -> bool:
        # может ли записаться на приём к врачу
        return True

    def can_be_admitted_to_ward(self) -> bool:
        # может быть положен в палату
        return True

    def can_complete_appointment(self) -> bool:
        # можно ли завершить приём у врача
        return False


class ScheduledForAppointmentState(PatientState):
    """Записан на приём."""

    key = "scheduled"
    label = "Записан на приём"

    def can_be_admitted_to_ward(self) -> bool:
        return True

    def can_complete_appointment(self) -> bool:
        return True


class HospitalizedState(PatientState):
    """Госпитализирован."""

    key = "hospitalized"
    label = "Госпитализирован"

    def can_complete_appointment(self) -> bool:
        return True

    def can_be_discharged(self) -> bool:
        return True


class DischargedState(PatientState):
    """Выписан; возможны повторная запись или повторная госпитализация."""

    key = "discharged"
    label = "Выписан"

    def can_schedule_appointment(self) -> bool:
        return True

    def can_be_admitted_to_ward(self) -> bool:
        return True


NOT_HOSPITALIZED = NotHospitalizedState()
SCHEDULED_FOR_APPOINTMENT = ScheduledForAppointmentState()
HOSPITALIZED = HospitalizedState()
DISCHARGED = DischargedState()

_STATE_BY_KEY = {
    NOT_HOSPITALIZED.key: NOT_HOSPITALIZED,
    SCHEDULED_FOR_APPOINTMENT.key: SCHEDULED_FOR_APPOINTMENT,
    HOSPITALIZED.key: HOSPITALIZED,
    DISCHARGED.key: DISCHARGED,
}


def patient_state_from_key(key: str) -> PatientState:
    if key not in _STATE_BY_KEY:
        raise ValueError(f"Неизвестное состояние: {key}")
    return _STATE_BY_KEY[key]
