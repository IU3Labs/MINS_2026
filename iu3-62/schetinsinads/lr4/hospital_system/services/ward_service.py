from __future__ import annotations

from typing import Sequence

from hospital_system.domain.entities import Ward
from hospital_system.repositories.interfaces import WardRepository
from hospital_system.utils import new_id

class WardService:
    def __init__(self, repo: WardRepository):
        self._repo = repo

    def create(self, number: str, name: str, capacity: int, is_icu: bool = False) -> Ward:
        ward = Ward(
            id=new_id(),
            number=number,
            name=name,
            capacity=capacity,
            is_icu=is_icu,
        )

        self._repo.add(ward)
        return ward

    def list(self) -> Sequence[Ward]:
        return self._repo.list()

    def get(self, ward_id: str) -> Ward:
        return self._repo.get(ward_id)