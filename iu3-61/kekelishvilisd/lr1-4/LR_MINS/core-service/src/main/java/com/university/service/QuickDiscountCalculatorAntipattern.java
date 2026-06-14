package com.university.service;


import com.university.exceptions.StudentNotFoundException;
import com.university.model.Student;
import com.university.model.Grade;
import com.university.repository.GradeRepository;
import com.university.repository.StudentRepository;

import java.util.List;

public class QuickDiscountCalculatorAntipattern {

    // MAGIC NUMBERS
    // значения без контекста и объяснений

    private static final double THRESHOLD_1 = 4.5;
    private static final double THRESHOLD_2 = 4.0;
    private static final double DISCOUNT_1 = 0.15;
    private static final double DISCOUNT_2 = 0.10;
    private static final int MIN_COUNT = 3;

    private final StudentRepository studentRepo;
    private final GradeRepository gradeRepo;

    public QuickDiscountCalculatorAntipattern(StudentRepository studentRepo, GradeRepository gradeRepo) {
        this.studentRepo = studentRepo;
        this.gradeRepo = gradeRepo;
    }

    public double calculateDiscount(int studentId) throws StudentNotFoundException {

        Student student = studentRepo.findById(studentId);
        List<Grade> grades = gradeRepo.findByStudentId(studentId);

        if (grades.size() < MIN_COUNT) {
            return 0.0;
        }

        double sum = 0;
        for (Grade g : grades) {
            sum += g.getValue();
        }
        double avg = sum / grades.size();

        if (avg >= THRESHOLD_1) {
            return DISCOUNT_1;
        } else if (avg >= THRESHOLD_2) {
            return DISCOUNT_2;
        }

        return 0.0;
    }

    public String getPromoCode(double discount) {
        if (discount == 0.15) return "EXCELLENT2026";
        if (discount == 0.10) return "GOOD2026";
        return "NO_DISCOUNT";
    }


    public String formatDiscount(double discount) {
        return (int)(discount * 100) + "%";
    }
}
