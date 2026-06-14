from datetime import datetime

from exceptions import HospitalException
from export_visitors import HospitalExportVisitor, JsonExportVisitor, TxtExportVisitor, save_export
from services import AppointmentService, DoctorService, PatientService, WardService


class HospitalSystemUI:
    def __init__(
        self,
        patient_service: PatientService,
        doctor_service: DoctorService,
        ward_service: WardService,
        appointment_service: AppointmentService,
    ) -> None:
        self.patient_service = patient_service
        self.doctor_service = doctor_service
        self.ward_service = ward_service
        self.appointment_service = appointment_service

        self.commands = {
            "1": self._register_patient,
            "2": self._admit_to_ward,
            "3": self._schedule_appointment,
            "4": self._complete_appointment,
            "5": self._show_all_patients,
            "6": self._add_ward,
            "7": self._show_all_wards,
            "8": self._add_doctor,
            "9": self._edit_doctor,
            "10": self._delete_doctor,
            "11": self._show_all_doctors,
            "12": self._show_all_appointments,
            "13": self._discharge_patient,
            "14": self._export_data,
        }

    def _register_patient(self) -> None:
        name = input("Введите имя пациента: ").strip()
        age = int(input("Введите возраст пациента: ").strip())
        patient = self.patient_service.register_patient(name, age)
        print(f"Пациент зарегистрирован. ID: {patient.id}, статус: {patient.state.label}")

    def _admit_to_ward(self) -> None:
        patient_id = int(input("Введите ID пациента: ").strip())
        ward_id = int(input("Введите ID палаты: ").strip())
        self.patient_service.admit_to_ward(patient_id, ward_id)
        print("Пациент успешно госпитализирован.")

    def _schedule_appointment(self) -> None:
        patient_id = int(input("Введите ID пациента: ").strip())
        doctor_id = int(input("Введите ID врача (например, 1): ").strip())
        dt = datetime.now()
        appointment = self.appointment_service.schedule_appointment(patient_id, doctor_id, dt)
        print(
            f"Приём назначен. ID приёма: {appointment.id}. "
            f"Статус пациента: {self.patient_service.patient_repo.get_by_id(patient_id).state.label}"
        )

    def _complete_appointment(self) -> None:
        appt_id = int(input("Введите ID приёма: ").strip())
        diagnosis = input("Введите диагноз: ").strip()
        prescription = input("Введите рецепт: ").strip()
        self.appointment_service.complete_appointment(appt_id, diagnosis, prescription)
        appt = self.appointment_service.appointment_repo.get_by_id(appt_id)
        patient = self.patient_service.patient_repo.get_by_id(appt.patient_id) if appt else None
        st = patient.state.label if patient else "?"
        print(f"Приём успешно завершён. Текущий статус пациента: {st}")

    def _show_all_patients(self) -> None:
        patients = self.patient_service.patient_repo.get_all()
        if not patients:
            print("Пациенты отсутствуют.")
            return

        for p in patients:
            history = ", ".join(p.diagnosis_history) if p.diagnosis_history else "отсутствует"
            ward = self.ward_service.get_ward(p.ward_id)
            ward_txt = ward.id if ward else "—"
            print(
                f"ID: {p.id} | Имя: {p.name} | Возраст: {p.age} | Статус: {p.state.label} | "
                f"Палата: {ward_txt} | История: {history}"
            )

    def _add_ward(self) -> None:
        number = int(input("Введите номер палаты: ").strip())
        capacity = int(input("Введите вместимость палаты: ").strip())
        ward = self.ward_service.add_ward(number, capacity)
        print(f"Палата добавлена. ID: {ward.id}")

    def _show_all_wards(self) -> None:
        wards = self.ward_service.list_wards()
        if not wards:
            print("Палаты отсутствуют.")
            return

        for w in wards:
            print(
                f"ID: {w.id}  Номер: {w.number}  Вместимость: {w.capacity} | "
                f"Текущие пациенты (ID): {w.current_patients}"
            )

    def _add_doctor(self) -> None:
        name = input("Введите имя врача: ").strip()
        specialization = input("Введите специализацию врача: ").strip()
        doctor = self.doctor_service.add_doctor(name, specialization)
        print(f"Врач добавлен. ID: {doctor.id}")

    def _edit_doctor(self) -> None:
        doctor_id = int(input("Введите ID врача: ").strip())
        name = input("Введите новое имя врача (оставьте пустым, чтобы не изменять): ").strip()
        specialization = input("Введите новую специализацию (оставьте пустым, чтобы не изменять): ").strip()
        self.doctor_service.update_doctor(
            doctor_id,
            name=name or None,
            specialization=specialization or None,
        )
        print("Данные врача обновлены.")

    def _delete_doctor(self) -> None:
        doctor_id = int(input("Введите ID врача для удаления: ").strip())
        self.doctor_service.delete_doctor(doctor_id)
        print("Врач удалён.")

    def _show_all_doctors(self) -> None:
        doctors = self.doctor_service.list_doctors()
        if not doctors:
            print("Врачи отсутствуют.")
            return

        for d in doctors:
            print(f"ID: {d.id}  Имя: {d.name}  Специализация: {d.specialization}")

    def _show_all_appointments(self) -> None:
        appointments = self.appointment_service.appointment_repo.get_all()
        if not appointments:
            print("Приёмы отсутствуют.")
            return

        for a in appointments:
            print(
                f"ID приёма: {a.id}  ID пациента: {a.patient_id}  ID врача: {a.doctor_id} | "
                f"Дата/время: {a.date_time}  Диагноз: {a.diagnosis}  Рецепт: {a.prescription}"
            )

    def _discharge_patient(self) -> None:
        try:
            patient_id = int(input("Введите ID пациента для выписки: ").strip())
            self.patient_service.discharge_patient(patient_id)
            print(f"✅ Пациент с ID {patient_id} успешно выписан.")
        except ValueError:
            print("🔢 Ошибка: введите корректный ID (число).")

    def _pick_visitor(self) -> HospitalExportVisitor:
        fmt = input("Формат экспорта: 1 — TXT, 2 — JSON: ").strip()
        if fmt == "2":
            return JsonExportVisitor()
        return TxtExportVisitor()

    def _export_data(self) -> None:
        print("Что экспортировать: 1 — пациенты, 2 — врачи, 3 — палаты, 4 — приёмы")
        choice = input("Выбор: ").strip()
        filename = input("Имя файла (без расширения, Enter — export_result): ").strip() or "export_result"
        visitor = self._pick_visitor()
        ext = ".json" if isinstance(visitor, JsonExportVisitor) else ".txt"
        path = f"{filename}{ext}"

        if choice == "1":
            data = visitor.visit_patients(self.patient_service.patient_repo.get_all())
        elif choice == "2":
            data = visitor.visit_doctors(self.doctor_service.list_doctors())
        elif choice == "3":
            data = visitor.visit_wards(self.ward_service.list_wards())
        elif choice == "4":
            data = visitor.visit_appointments(self.appointment_service.appointment_repo.get_all())
        else:
            print("Неизвестный тип данных.")
            return
        save_export(data, path)
        print(f"Файл сохранён: {path}")


    def _print_menu(self) -> None:
        print("\nИнформационная система больницы")
        print("1. Регистрация пациента")
        print("2. Госпитализировать пациента")
        print("3. Записать пациента на приём")
        print("4. Завершить приём пациента (диагноз/рецепт)")
        print("5. Показать всех пациентов")
        print("6. Добавить палату")
        print("7. Показать список палат")
        print("8. Добавить врача")
        print("9. Изменить врача")
        print("10. Удалить врача")
        print("11. Показать список врачей")
        print("12. Показать список приёмов")
        print("13. Выписать пациента из стационара")
        print("14. Экспорт данных (Visitor: TXT / JSON)")
        print("0. Выход")

    def run(self) -> None:
        while True:
            self._print_menu()
            choice = input("Выберите пункт меню: ").strip()
            if choice == "0":
                print("Завершение работы системы.")
                break
            try:
                action = self.commands.get(choice)
                if action:
                    action()
                else:
                    print("⚠️ Неизвестный пункт меню. Попробуйте снова.")

            except HospitalException as e:
                print(f"❌ {e.get_user_message()}")
            except ValueError:
                print("🔢 Ошибка ввода. Вводите только числа там, где это требуется (ID, возраст, номер палаты).")
            except Exception as e:
                print(f"Произошла непредвиденная ошибка: {e}")
