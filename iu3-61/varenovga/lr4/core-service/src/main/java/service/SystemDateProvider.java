package service;

import interfaces.DateProvider;
import java.time.LocalDate;

public class SystemDateProvider implements DateProvider {
    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}