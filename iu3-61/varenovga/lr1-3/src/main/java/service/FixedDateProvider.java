package main.java.service;

import main.java.interfaces.DateProvider;
import java.time.LocalDate;

public class FixedDateProvider implements DateProvider {
    private final LocalDate fixedDate;

    public FixedDateProvider(LocalDate fixedDate) {
        this.fixedDate = fixedDate;
    }

    @Override
    public LocalDate now() {
        return fixedDate;
    }
}