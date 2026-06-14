package org.example.coreservice.ui;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ScheduleExporter {

    private final InputUtil inputUtil;

    public ScheduleExporter(InputUtil inputUtil) {
        this.inputUtil = inputUtil;
    }

    public void format(String schedule) {
        var flag = false;

        while (!flag) {
            System.out.println("\nВыберите формат вывода: ");
            System.out.println("1. Файл .txt");
            System.out.println("2. Файл .pdf");
            var choice = inputUtil.readInt("Ввод: ");

            switch (choice) {
                case 1:
                    formatTxt(schedule);
                case 2:
                    if (choice == 2) {
                        formatPdf(schedule);
                    }
                    flag = true;
                    break;
                default:
                    break;
            }
        }

    }

    private void formatTxt(String schedule) {
        System.out.println("\nВведите имя файла без расширения: ");
        String fileName = new java.util.Scanner(System.in).nextLine();

        try {
            FileWriter fw = new FileWriter(fileName + ".txt");
            fw.write(schedule);
            fw.close();
            System.out.println("Сохранено!");
        } catch (Exception e) {

        }
    }

    private void formatPdf(String schedule) {
        System.out.println("\nВведите имя файла без расширения: ");
        String fileName = new java.util.Scanner(System.in).nextLine();

        PDDocument document = null;
        PDPageContentStream stream = null;
        try {
            document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            InputStream fontStream = getClass().getResourceAsStream("/fonts/Rubik-SemiBold.ttf");
            PDType0Font font = null;
            if (fontStream != null) {
                font = PDType0Font.load(document, fontStream);
            }

            stream = new PDPageContentStream(document, page);

            PDImageXObject image = PDImageXObject.createFromFile("schedule.jpg", document);
            stream.drawImage(image, 50, 750 - 150, 500, 150);

            stream.beginText();
            float textY = 750 - 200;
            stream.newLineAtOffset(50, textY);
            stream.setFont(font, 10);

            String[] lines = schedule.split("\n");
            int lineHeight = 15;

            for (String line : lines) {
                String cleanLine = line.replace("\r", "").trim();
                if (cleanLine.isEmpty()) {
                    textY -= lineHeight;
                    continue;
                }

                if (textY < 50) {
                    stream.endText();
                    stream.close();

                    page = new PDPage();
                    document.addPage(page);
                    stream = new PDPageContentStream(document, page);
                    stream.beginText();
                    stream.newLineAtOffset(50, 750);
                    stream.setFont(font, 10);
                    textY = 750;
                }

                stream.showText(cleanLine);
                stream.newLineAtOffset(0, -lineHeight);
                textY -= lineHeight;
            }

            stream.endText();
            stream.close();
            document.save(fileName + ".pdf");
            document.close();

            System.out.println("PDF создан: " + fileName + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
