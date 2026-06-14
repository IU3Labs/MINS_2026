package com.university.service.export;

import com.university.model.Grade;
import com.university.service.report.Report;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TextFileExportStrategy implements ReportExportStrategy {
    private static final String FOLDER_PATH = "exports" + File.separator;
    private static final String FILE_PATH = FOLDER_PATH + "report_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

    @Override
    public void export(Report report) {
        new File(FOLDER_PATH).mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(report.getTitle());
            writer.println("=".repeat(report.getTitle().length()));
            for (String row : report.getRows()) {
                writer.println(row);
            }
            writer.println("-".repeat(report.getTitle().length()));
            writer.println(report.getSummary());
            writer.println("\nДата экспорта: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

            System.out.println("Отчет сохранен: " + new File(FILE_PATH).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getFormatName() {
        return "TEXT_FILE";
    }
}
