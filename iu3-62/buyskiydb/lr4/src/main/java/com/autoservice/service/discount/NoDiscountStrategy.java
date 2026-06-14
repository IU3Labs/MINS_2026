package com.autoservice.service.discount;

import com.autoservice.model.Service;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculatePrice(Service service, int clientVisitsCount) {
        return service.getPrice();
    }
}