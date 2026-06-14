package service.pricing;

import entity.Ticket;

import java.util.List;

public interface IPricingService {
    void setStrategy(String strategyId);
    void setStrategyByIndex(int index);
    double calculatePrice(Ticket ticket);
    String getCategoryName();
    List<PricingStrategy> getStrategies();
    String getMenu();
}