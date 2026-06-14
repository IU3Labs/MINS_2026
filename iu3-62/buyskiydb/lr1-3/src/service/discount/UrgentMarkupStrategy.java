package service.discount;

import model.Service;

public class UrgentMarkupStrategy implements DiscountStrategy {
    @Override
    public double calculatePrice(Service service, int clientVisitsCount) {
        return service.getPrice() * 1.20;
    }
}