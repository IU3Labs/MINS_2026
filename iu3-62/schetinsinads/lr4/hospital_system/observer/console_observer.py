from hospital_system.observer.observer import Observer

class ConsoleObserver(Observer):
    def update(self, event: str, data):
        print(f"[EVENT] {event}: {data}")