package org.example.coreservice.ui.strategy;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.ui.formatter.ScheduleRowFormatter;
import org.example.coreservice.ui.formatter.WeekdayFormatter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.coreservice.ui.formatter.WeekdayFormatter.formatWeekday;


@Component
public class SortByDayAndTime implements ScheduleSortStrategy {

    @Override
    public int key() {
        return 1;
    }

    @Override
    public String label() {
        return "По дню и времени";
    }


    @Override
    public String format(List<ScheduleEntry> entries,
                         ScheduleRowFormatter formatter,
                         Map<Long, String> teacherNames,
                         Map<Long, String> classNames) {
        StringBuilder sb = new StringBuilder();

        entries.stream()
                .sorted(Comparator.comparing(ScheduleEntry::getDayOfWeek)
                        .thenComparing(ScheduleEntry::getStartTime))
                .collect(Collectors.groupingBy(ScheduleEntry::getDayOfWeek,
                        LinkedHashMap::new, Collectors.toList()))
                .forEach((day, dayEntries) -> {
                    sb.append("\n--- ")
                            .append(formatWeekday(day, WeekdayFormatter.Format.LONG))
                            .append(" ---\n");
                    dayEntries.forEach(e -> sb.append(
                            formatter.formatRow(
                                    e,
                                    teacherNames.getOrDefault(e.getTeacherId(), "—"),
                                    classNames.getOrDefault(e.getSchoolClassId(), "—")
                            )
                    ).append("\n"));
                });

        return sb.toString();
    }


}
