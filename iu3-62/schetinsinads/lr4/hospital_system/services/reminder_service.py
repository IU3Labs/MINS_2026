from __future__ import annotations

from typing import Sequence

from hospital_system.domain.entities import Appointment
from hospital_system.repositories.interfaces import AppointmentRepository
from hospital_system.utils import today
from hospital_system.utils import today
from hospital_system.strategy.days_strategy import DaysStrategy

class ReminderService:
    def __init__(self, appointments: AppointmentRepository):
        self._appointments = appointments

    def upcoming_within(self, days: int):
        strategy = DaysStrategy(days)
        out = strategy.filter(self._appointments.list(), today())
        out.sort(key=lambda x: (x[1], x[0].starts_at))
        return out