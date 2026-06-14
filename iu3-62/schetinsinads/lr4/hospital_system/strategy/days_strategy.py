from datetime import date
from typing import Sequence
from hospital_system.domain.entities import Appointment
from hospital_system.strategy.reminder_strategy import ReminderStrategy

class DaysStrategy(ReminderStrategy):
    def __init__(self, days: int):
        self._days = days

    def filter(self, appointments: Sequence[Appointment], today: date):
        result = []
        for appt in appointments:
            delta = (appt.starts_at.date() - today).days
            if 0 <= delta <= self._days:
                result.append((appt, delta))
        return result