package com.university.service;

import com.university.model.Grade;
import java.util.List;

public class GradeAverageCalculator {


    public double calculateAverage(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getValue();
        }
        return sum / grades.size();
    }

    public double calculateWeightedAverage(List<Grade> grades, List<Double> weights) {
        if (grades == null || grades.isEmpty() || grades.size() != weights.size()) {
            return calculateAverage(grades);
        }

        double weightedSum = 0;
        double weightTotal = 0;

        for (int i = 0; i < grades.size(); i++) {
            weightedSum += grades.get(i).getValue() * weights.get(i);
            weightTotal += weights.get(i);
        }

        return weightTotal > 0 ? weightedSum / weightTotal : 0.0;
    }
}
