package org.example.coreservice.ui.strategy;


import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.ui.formatter.ScheduleRowFormatter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SortBySubject implements ScheduleSortStrategy {

    @Override
    public int key() {
        return 2;
    }

    @Override
    public String label() {
        return "По предмету";
    }

    @Override
    public String format(List<ScheduleEntry> entries,
                         ScheduleRowFormatter formatter,
                         Map<Long, String> teacherNames,
                         Map<Long, String> classNames) {
        StringBuilder sb = new StringBuilder();

        entries.stream()
                .sorted(Comparator.comparing(ScheduleEntry::getTitle)
                        .thenComparing(ScheduleEntry::getDayOfWeek))
                .collect(Collectors.groupingBy(ScheduleEntry::getTitle,
                        LinkedHashMap::new, Collectors.toList()))
                .forEach((subject, subjectEntries) -> {
                    sb.append("\n--- ").append(subject).append(" ---\n");
                    subjectEntries.forEach(e -> sb.append(
                            formatter.formatRowWithDay(
                                    e,
                                    teacherNames.getOrDefault(e.getTeacherId(), "—"),
                                    classNames.getOrDefault(e.getSchoolClassId(), "—")
                            )
                    ).append("\n"));
                });

        return sb.toString();
    }
}
