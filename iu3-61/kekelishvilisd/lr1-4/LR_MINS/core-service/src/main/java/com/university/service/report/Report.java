package com.university.service.report;

import java.util.List;


public interface Report {
    String getTitle();
    List<String> getRows();
    String getSummary();
}
