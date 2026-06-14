package com.university.service.report;

import com.university.model.Grade;
import java.util.List;
import java.util.stream.Collectors;


public class GradeReport implements Report {
    private final int studentId;
    private final String studentName;
    private final List<Grade> grades;

    public GradeReport(int studentId, String studentName, List<Grade> grades) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.grades = grades;
    }

    @Override
    public String getTitle() {
        return "ВЕДОМОСТЬ ОЦЕНОК | Студент: " + studentName + " (ID:" + studentId + ")";
    }

    @Override
    public List<String> getRows() {
        return grades.stream()
                .map(g -> g.getCourse().getName() + ": " + g.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public String getSummary() {
        return "Всего предметов: " + grades.size();
    }
}
