package com.university.service;

import com.university.exceptions.*;
import com.university.model.*;
import com.university.repository.*;
import com.university.service.export.ReportExportStrategy;
import com.university.service.observer.NotificationManager;
import com.university.service.report.*;

import java.util.ArrayList;
import java.util.List;

public class AssessmentServiceImpl implements AssessmentService {
    private final StudentRepository studentRepo;
    //private final List<Grade> gradesDb = new ArrayList<>();
    private final NotificationManager notificationManager;
    private final ReportFactory reportFactory;
    private final GradeRepository gradeRepo;

    public AssessmentServiceImpl(StudentRepository studentRepo,
                                 NotificationManager notificationManager,
                                 ReportFactory reportFactory, GradeRepository gradeRepository) {
        this.studentRepo = studentRepo;
        this.notificationManager = notificationManager;
        this.reportFactory = reportFactory;
        this.gradeRepo = gradeRepository;
    }

    @Override
    public void addGrade(int studentId, String courseName, int value)
            throws StudentNotFoundException, InvalidGradeException {
        if (value < 1 || value > 5) {
            throw new InvalidGradeException(value, 1, 5);
        }
        Student s = studentRepo.findById(studentId);
        Grade grade = new Grade(s, new Course(courseName), value);
        gradeRepo.save(grade);

        notificationManager.notifyObserver(s,
                "Новая оценка по \"" + courseName + "\": " + value);
    }


    @Override
    public Report getGradeReport(int studentId) throws StudentNotFoundException {
        Student student = studentRepo.findById(studentId);
        List<Grade> grades = gradeRepo.findByStudentId(studentId);

        return reportFactory.createReport(
                ReportFactory.ReportType.GRADES,
                studentId,
                student.getName(),
                grades,
                new ArrayList<>()
        );
    }

    @Override
    public void exportReport(Report report, ReportExportStrategy strategy) {
        strategy.export(report);
    }
}
