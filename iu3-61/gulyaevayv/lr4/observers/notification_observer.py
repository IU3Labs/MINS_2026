import logging


class NotificationObserver:
    def update(self, event_name, payload):
        raise NotImplementedError


class LogNotificationObserver(NotificationObserver):
    def update(self, event_name, payload):
        logging.info("%s: %s", event_name, payload)


class ConsoleNotificationObserver(NotificationObserver):
    def update(self, event_name, payload):
        messages = {
            "medicine_added": "Добавлено лекарство",
            "medicine_sold": "Продано лекарство",
            "medicine_removed": "Списано просроченное лекарство",
        }

        message = messages.get(event_name)
        if message:
            print(f"{message}: {payload}")
