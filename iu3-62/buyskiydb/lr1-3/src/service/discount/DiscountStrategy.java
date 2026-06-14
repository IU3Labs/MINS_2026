package service.discount;

import model.Service;

public interface DiscountStrategy {
    double calculatePrice(Service service, int clientVisitsCount);
}