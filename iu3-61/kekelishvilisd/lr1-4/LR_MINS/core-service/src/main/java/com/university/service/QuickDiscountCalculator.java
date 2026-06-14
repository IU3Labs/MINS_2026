package com.university.service;


import com.university.exceptions.StudentNotFoundException;
import com.university.model.Student;
import com.university.model.Grade;
import com.university.repository.GradeRepository;
import com.university.repository.StudentRepository;
import java.util.List;

public class QuickDiscountCalculator {


    private final DiscountConfig config;

    public QuickDiscountCalculator(DiscountConfig config) {

        this.config = config;
    }

    public int calculateDiscountPercent(double average, int gradesCount) {
        if (gradesCount < config.getMinGradesCount()) {
            return 0;
        }

        if (average >= config.getExcellentThreshold()) return config.getExcellentDiscountPercent();
        if (average >= config.getGoodThreshold()) return config.getGoodDiscountPercent();

        return 0;
    }

    public String getPromoCode(double discount) {
        if (discount == config.getExcellentDiscountPercent()) return config.getExcellentPromo();
        if (discount == config.getGoodDiscountPercent()) return config.getGoodPromo();
        return config.getNoDiscountPromo();
    }

    public String formatDiscount(int discountPercent) {
        return discountPercent + "%";
    }

    public int getMinGradesCount() {
        return config.getMinGradesCount();
    }

    public double getGoodThreshold() {
        return config.getGoodThreshold();
    }
}