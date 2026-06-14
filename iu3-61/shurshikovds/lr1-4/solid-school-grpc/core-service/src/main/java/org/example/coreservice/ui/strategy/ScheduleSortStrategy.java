package org.example.coreservice.ui.strategy;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.ui.formatter.ScheduleRowFormatter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ScheduleSortStrategy {
    String format(List<ScheduleEntry> entries,
                  ScheduleRowFormatter formatter,
                  Map<Long, String> teacherNames,
                  Map<Long, String> classNames);
    int key();
    String label();
}
