package org.example.coreservice.ui.formatter;


import org.example.coreservice.entity.ScheduleEntry;
import org.springframework.stereotype.Component;

@Component
public class AdminRowFormatter implements ScheduleRowFormatter {

    @Override
    public String formatRow(ScheduleEntry e, String teacherName, String className) {
        return String.format(" %-8s %-25s %-20s %-10s",
                e.getStartTime(), e.getTitle(), teacherName, e.getRoom());
    }

    @Override
    public String formatRowWithDay(ScheduleEntry e, String teacherName, String className) {
        return String.format(" %-12s %-8s %-25s %-20s %-10s",
                WeekdayFormatter.formatWeekday(e.getDayOfWeek(), WeekdayFormatter.Format.LONG),
                e.getStartTime(), e.getTitle(), teacherName, e.getRoom());
    }
}

