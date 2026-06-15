import csv
import os


class CsvRepository:
    def __init__(self, file_path):
        self.file_path = file_path

    def read_rows(self):
        try:
            with open(self.file_path, newline="", encoding="utf-8") as file:
                return list(csv.reader(file))
        except FileNotFoundError:
            return []

    def write_rows(self, rows):
        directory = os.path.dirname(self.file_path)
        if directory:
            os.makedirs(directory, exist_ok=True)

        with open(self.file_path, "w", newline="", encoding="utf-8") as file:
            writer = csv.writer(file)
            writer.writerows(rows)
