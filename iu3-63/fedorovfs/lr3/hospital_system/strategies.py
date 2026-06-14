import json
from abc import ABC, abstractmethod
from typing import List, Any
from exceptions import HospitalException


class ExportStrategy(ABC):
    @abstractmethod
    def export(self, data: List[Any], filename: str = None) -> str:
        pass


class TextExportStrategy(ExportStrategy):
    def export(self, data: List[Any], filename: str = None) -> str:
        try:
            result = "--- ОТЧЕТ БОЛЬНИЦЫ ---\n"
            result += "\n".join([f"• {str(item)}" for item in data])

            if filename:
                if not filename.endswith('.txt'): filename += '.txt'
                with open(filename, 'w', encoding='utf-8') as f:
                    f.write(result)
            return result
        except IOError as e:
            raise HospitalException(f"Ошибка при сохранении TXT файла: {e}")


def _item_as_dict(item: Any) -> dict:
    d = vars(item).copy()
    st = d.get("state")
    if st is not None and hasattr(st, "key") and hasattr(st, "label"):
        d["state"] = st.label
        d["state_key"] = st.key
    return d


class JsonExportStrategy(ExportStrategy):
    def export(self, data: List[Any], filename: str = None) -> str:
        try:
            list_of_dicts = [_item_as_dict(item) for item in data]
            json_data = json.dumps(list_of_dicts, indent=4, ensure_ascii=False, default=str)

            if filename:
                if not filename.endswith('.json'): filename += '.json'
                with open(filename, 'w', encoding='utf-8') as f:
                    f.write(json_data)
            return json_data
        except (TypeError, IOError) as e:
            # TypeError может возникнуть, если данные нельзя сериализовать в JSON
            raise HospitalException(f"Ошибка при формировании JSON: {e}")