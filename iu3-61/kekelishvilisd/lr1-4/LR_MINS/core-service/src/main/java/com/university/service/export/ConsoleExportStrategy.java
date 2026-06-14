package com.university.service.export;


import com.university.model.Grade;
import com.university.service.report.Report;

import java.util.List;


public class ConsoleExportStrategy implements ReportExportStrategy {
    @Override
    public void export(Report report) {
        System.out.println("\n" + report.getTitle());
        System.out.println("=".repeat(report.getTitle().length()));
        for (String row : report.getRows()) {
            System.out.println(row);
        }
        System.out.println("-".repeat(report.getTitle().length()));
        System.out.println(report.getSummary());
    }

    @Override
    public String getFormatName() {
        return "CONSOLE";
    }
}