package org.example.coreservice.ui.formatter;


import org.example.coreservice.entity.ScheduleEntry;

public interface ScheduleRowFormatter {
    String formatRow(ScheduleEntry e, String teacherName, String className);
    String formatRowWithDay(ScheduleEntry e, String teacherName, String className);
}
