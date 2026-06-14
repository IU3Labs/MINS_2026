package com.university.service;

import com.university.model.*;
import com.university.repository.*;
import com.university.exceptions.*;
import com.university.service.export.ReportExportStrategy;
import com.university.service.report.Report;

import java.util.ArrayList;
import java.util.List;

public interface AssessmentService {
    void addGrade(int studentId, String courseName, int value)
            throws StudentNotFoundException, InvalidGradeException;
    Report getGradeReport(int studentId) throws StudentNotFoundException;
    void exportReport(Report report, ReportExportStrategy strategy);
}
