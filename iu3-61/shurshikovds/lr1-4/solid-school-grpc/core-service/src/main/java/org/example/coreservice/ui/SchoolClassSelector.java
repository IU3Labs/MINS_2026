package org.example.coreservice.ui;

import lombok.RequiredArgsConstructor;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.reference.grpc.SchoolClassDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class SchoolClassSelector extends BaseConsoleUI {

    private final ReferenceServiceGateway referenceGateway;

    public SchoolClassSelector(ReferenceServiceGateway referenceGateway, Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.referenceGateway = referenceGateway;
    }

    public Long ask(String traceId) {
        List<SchoolClassDto> classes = referenceGateway.listSchoolClasses(traceId);
        if (classes.isEmpty()) {
            System.out.println("Классов нет");
            return null;
        }

        for (int i = 0; i < classes.size(); i++) {
            SchoolClassDto c = classes.get(i);
            System.out.printf("%d. %d%s%n", i + 1, c.getGrade(), c.getLetter());
        }

        int index = inputUtil.readInt("Выберите класс (номер): ") - 1;
        if (index < 0 || index >= classes.size()) {
            System.out.println("Неверный номер");
            return null;
        }
        return classes.get(index).getId();
    }
}

