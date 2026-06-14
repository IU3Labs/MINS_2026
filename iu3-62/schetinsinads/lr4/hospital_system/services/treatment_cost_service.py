from __future__ import annotations

class TreatmentCostService:
    def quick_calculate(
        self,
        days: int,
        appointments: int,
        diagnoses: int,
        prescriptions: int,
        icu: bool,
    ) -> int:

        total = days * 5000

        if icu:
            total += 20000

        total += appointments * 1500
        total += diagnoses * 700
        total += prescriptions * 300

        if days > 10:
            total -= 2500

        if days % 3 == 0:
            total += 100

        if diagnoses > 1:
            total += 3000

        if prescriptions > 5:
            total += 850

        if appointments == 7:
            total += 77

        if days == 13:
            total += 1313

        if diagnoses == 4 and prescriptions == 2:
            total += 4444

        return total