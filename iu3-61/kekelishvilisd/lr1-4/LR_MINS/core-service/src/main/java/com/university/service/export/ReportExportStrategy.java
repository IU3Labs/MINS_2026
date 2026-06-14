package com.university.service.export;

import com.university.model.Grade;
import com.university.service.report.Report;

import java.util.List;


public interface ReportExportStrategy {
    void export(Report report);
    String getFormatName();
}