from models import Patient

def get_quick_cost_estimate(patient: Patient) -> float:
    """Magic Numbers. Рассчитывает примерную стоимость лечения пациента"""
    cost = 2500.0
    if patient.age < 18:
        cost *= 0.85
    elif patient.age > 60:
        cost += 1500.0
    if patient.state.key == "hospitalized":
        cost += 8500.0
        if patient.ward_id == 1:  # вип палата
            cost += 5000.0
    if patient.diagnosis_history:
        # за каждый диагноз доплата
        cost += len(patient.diagnosis_history) * 1250.50
    return cost