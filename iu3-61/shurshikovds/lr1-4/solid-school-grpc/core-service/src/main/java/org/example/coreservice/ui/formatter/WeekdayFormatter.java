package org.example.coreservice.ui.formatter;


import org.example.coreservice.entity.enums.Weekday;

public final class WeekdayFormatter {

    public enum Format { LONG, SHORT }

    private WeekdayFormatter() {}

    public static String formatWeekday(Weekday day, Format format) {
        return switch (day) {
            case monday    -> format == Format.SHORT ? "Пн" : "Понедельник";
            case tuesday   -> format == Format.SHORT ? "Вт" : "Вторник";
            case wednesday -> format == Format.SHORT ? "Ср" : "Среда";
            case thursday  -> format == Format.SHORT ? "Чт" : "Четверг";
            case friday    -> format == Format.SHORT ? "Пт" : "Пятница";
            case saturday  -> format == Format.SHORT ? "Сб" : "Суббота";
            case sunday    -> format == Format.SHORT ? "Вс" : "Воскресенье";
        };
    }
}
