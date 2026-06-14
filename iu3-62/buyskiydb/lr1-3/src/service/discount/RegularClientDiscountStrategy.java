package service.discount;

import model.Service;

public class RegularClientDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculatePrice(Service service, int clientVisitsCount) {
        double price = service.getPrice();
        if (clientVisitsCount > 1) {
            price = price * 0.95;
        }
        return price;
    }
}