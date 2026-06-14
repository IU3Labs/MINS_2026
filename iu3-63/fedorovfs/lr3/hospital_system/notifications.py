from abc import ABC, abstractmethod
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any, Dict, List

from exceptions import EmailDeliveryFailedException, EmailInvalidRecipientException, EmailNotificationException
from notification_event_types import NotificationEventType


class EventManager:
    def __init__(self) -> None:
        self._observers: List[Observer] = []

    def subscribe(self, observer) -> None:
        self._observers.append(observer)

    def notify(self, event) -> None:
        for observer in self._observers:
            try:
                observer.update(event)
            except EmailNotificationException as exc:
                print(f"[почта не доставлена] {exc.get_user_message()}")


@dataclass
class NotificationEvent:
    """Система уведомлений"""

    event_type: str
    message: str
    payload: Dict[str, Any] = field(default_factory=dict)


class Observer(ABC):
    @abstractmethod
    def update(self, event: NotificationEvent) -> None:
        pass


class FileAndConsoleLogObserver(Observer):
    """Пишет уведомления в файл и дублирует в консоль"""

    def __init__(self, log_file: str = "hospital_notifications.log.txt") -> None:
        path = Path(log_file)
        if not path.is_absolute():
            path = Path(__file__).resolve().parent / path.name
        self._log_file = path

    def update(self, event: NotificationEvent) -> None:
        line = f"[{event.event_type}] {event.message}"
        if event.payload:
            line += f" | {event.payload}"
        self._log_file.parent.mkdir(parents=True, exist_ok=True)
        with open(self._log_file, "a", encoding="utf-8") as f:
            f.write(line + "\n")
            f.flush()


class EmailMockObserver(Observer):
    """Имитация отправки e-mail"""

    @staticmethod
    def _patient_email(name: str) -> str:
        safe = "".join(c if c.isalnum() else "_" for c in name.lower()) or "patient"
        return f"{safe}@yandex.ru"

    @staticmethod
    def _require_patient_name(payload: Dict[str, Any]) -> str:
        name = str(payload.get("patient_name") or "").strip()
        if not name:
            raise EmailInvalidRecipientException(
                "не указано имя пациента, нельзя построить адрес",
                recipient_hint=str(payload.get("patient_id", "")),
            )
        return name

    def _emit_preview(self, text: str) -> None:
        try:
            print(text)
        except UnicodeEncodeError as exc:
            raise EmailDeliveryFailedException(
                "ошибка отправки email",
                technical_detail=str(exc),
            ) from exc

    def update(self, event: NotificationEvent) -> None:
        p = event.payload
        if event.event_type == NotificationEventType.HOSPITALIZED:
            self._require_patient_name(p)
            if p.get("ward_id") is None:
                raise EmailInvalidRecipientException(
                    "в письме о госпитализации нет ID палаты",
                    recipient_hint=str(p.get("patient_name")),
                )
            to = self._patient_email(str(p.get("patient_name")))
            subj = "Госпитализация"
            body = (
                f"Здравствуйте! Вы размещены в палате №{p.get('ward_number')} "
                f"(ID палаты {p.get('ward_id')}). ID пациента: {p.get('patient_id')}."
            )
        elif event.event_type == NotificationEventType.APPOINTMENT_COMPLETED:
            self._require_patient_name(p)
            to = self._patient_email(str(p.get("patient_name")))
            subj = "Результаты приёма"
            body = (
                f"Диагноз: {p.get('diagnosis')}. "
                f"Назначения: {p.get('prescription')}. "
                f"Дата приёма: {p.get('appointment_date')}"
            )
        elif event.event_type == NotificationEventType.PATIENT_STATE_CHANGED:
            self._require_patient_name(p)
            to = self._patient_email(str(p.get("patient_name")))
            subj = "Изменение статуса в поликлинике"
            body = f"Ваш статус изменён: {p.get('old_label')} -> {p.get('new_label')}"
        else:
            to = "admin@yandex.ru"
            subj = f"Уведомление: {event.event_type}"
            body = event.message
            if p:
                body += f" | {p}"

        preview = f"\n[EMAIL] To: {to}\nSubject: {subj}\n{body}\n"
        self._emit_preview(preview)

