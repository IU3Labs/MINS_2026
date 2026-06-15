from app import PharmacyApp


def menu():
    app = PharmacyApp()

    while True:
        print("1 Добавить лекарство")
        print("2 Показать лекарства")
        print("3 Найти")
        print("4 Продать")
        print("5 Проверить срок")
        print("6 Списать")
        print("7 Продажи")
        print("0 Выход")

        choice = input("Выбор: ")

        if choice == "0":
            app.save()
            print("Сохранено")
            break

        app.execute(choice)


if __name__ == "__main__":
    menu()
