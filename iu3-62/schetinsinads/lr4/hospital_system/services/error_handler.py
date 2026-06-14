class ErrorHandler:
    def handle(self, e: Exception) -> None:
        print(f"Ошибка: {e}")