from datetime import date
from typing import Sequence
from hospital_system.domain.entities import Appointment

class ReminderStrategy:
    def filter(self, appointments: Sequence[Appointment], today: date):
        raise NotImplementedError