package ru.iu3.ui.constants;

public final class UiConstants {

    public static final String PROMPT_MENU = "Выберите пункт меню: ";
    public static final String ERROR_PREFIX = "Произошла ошибка: ";
    public static final String INVALID_CHOICE = "Неверный выбор. Пожалуйста, попробуйте снова.";
    public static final String WELCOME_MESSAGE = "--- Добро пожаловать в чудный консольный коворкинг! ---";
    public static final String CHOICE_MESSAGE = "Вы выбрали '%s'";
    public static final String GOODBYE_MESSAGE = "Споки-ноки :3c ~";
    
    public static final String MAIN_ROOMS = "Комнаты";
    public static final String MAIN_BOOKINGS = "Бронирования";
    public static final String MAIN_PASSES = "Пропуска";
    public static final String MAIN_GENERATE_DATA = "Сгенерировать тестовые данные";
    public static final String MAIN_EXIT = "Выход";

    public static final String ROOMS_LIST_TITLE = "Cписок комнат:";
    public static final String ROOMS_EMPTY = "Список комнат пуст.";
    public static final String ROOMS_TABLE_HEADER = "ID | Тип | Название | Ставка (руб/час)";
    public static final String ROOM_LOCKED_SUFFIX = " (Запечатана)";
    public static final String ROOM_ROW_FORMAT = "%d | %s | %s | %d";
    public static final String ROOM_MENU_SHOW = "Список комнат";
    public static final String ROOM_MENU_ADD_WORKPLACE = "Добавить рабочее место";
    public static final String ROOM_MENU_ADD_MEETING = "Добавить переговорную";
    public static final String ROOM_MENU_LOCK = "Запечатать комнату";
    public static final String ROOM_MENU_BACK = "Назад";

    public static final String PROMPT_WORKPLACE_ID = "Введите ID рабочего места:";
    public static final String PROMPT_WORKPLACE_NAME = "Введите название рабочего места:";
    public static final String PROMPT_HOURLY_RATE = "Введите ставку аренды (руб/час):";
    public static final String PROMPT_MEETING_ID = "Введите ID переговорной:";
    public static final String PROMPT_MEETING_NAME = "Введите название переговорной:";
    public static final String PROMPT_LOCK_ROOM_ID = "Введите ID комнаты для запечатывания:";

    public static final String PASSES_LIST_TITLE = "Cписок пропусков:";
    public static final String PASS_LINE_FORMAT = "ID: %d, Владелец: %s, Активен: %s";
    public static final String PASS_MENU_SHOW = "Список пропусков";
    public static final String PASS_MENU_ISSUE = "Добавить пропуск";
    public static final String PASS_MENU_DEACTIVATE = "Удалить пропуск";
    public static final String PASS_MENU_ROOMS_FOR_PASS = "Помещения для пропуска";
    public static final String PASS_MENU_BACK = "Назад";

    public static final String PROMPT_PASS_ID = "Введите ID пропуска:";
    public static final String PROMPT_PASS_HOLDER = "Введите имя владельца пропуска:";
    public static final String PROMPT_PASS_DEACTIVATE = "Введите ID пропуска для деактивации:";
    public static final String PASS_NO_BOOKINGS = "Для этого пропуска нет активных бронирований.";
    public static final String PASS_ROOMS_ACCESS_HEADER = "Помещения, к которым есть доступ по этому пропуску:";
    public static final String PASS_ROOM_ROW_FORMAT = "%d | %s | %s";

    public static final String BOOKING_LINE_FORMAT = "ID: %d, ID Комнаты: %d, Время: c %s по %s, ID Пропуска: %d";
    public static final String BOOKING_MENU_SHOW = "Список бронирований";
    public static final String BOOKING_MENU_ADD = "Добавить бронирование";
    public static final String BOOKING_MENU_CANCEL = "Удалить бронирование";
    public static final String BOOKING_MENU_BACK = "Назад";

    public static final String PROMPT_BOOKING_ROOM_ID = "Введите ID комнаты:";
    public static final String PROMPT_BOOKING_MONTH = "Введите месяц (1-12):";
    public static final String PROMPT_BOOKING_DAY = "Введите день месяца:";
    public static final String PROMPT_BOOKING_START_TIME = "Введите время начала (HH:mm):";
    public static final String PROMPT_BOOKING_END_TIME = "Введите время окончания (HH:mm):";
    public static final String PROMPT_BOOKING_PASS_ID = "Введите ID пропуска:";

    public static final String BOOKING_CREATED = "Бронирование создано. Стоимость: ";
    public static final String BOOKING_CREATED_SUFFIX = " руб.";
    public static final String PROMPT_BOOKING_CANCEL = "Введите ID бронирования для отмены:";

    public static final String TEST_DATA_SUCCESS = "Тестовые данные успешно созданы.";
}
