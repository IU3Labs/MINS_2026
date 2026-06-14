package com.autoservice.service.discount;

import com.autoservice.model.Service;

public interface DiscountStrategy {
    double calculatePrice(Service service, int clientVisitsCount);
}