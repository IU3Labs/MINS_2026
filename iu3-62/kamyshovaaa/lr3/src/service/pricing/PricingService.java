package service.pricing;

import exception.ValidationException;

import java.util.List;

public class PricingService implements IPricingService {
    private final List<PricingStrategy> allStrat;
    private PricingStrategy currStrategy;

    public PricingService(List<PricingStrategy> strategies) {
        if (strategies == null || strategies.isEmpty()) {
            throw new ValidationException("Стратегии ценообразования не могут быть null или пустыми");
        }
        this.allStrat = List.copyOf(strategies);
        this.currStrategy = allStrat.getFirst();
    }

    @Override
    public void setStrategy(String strategyId) {
        this.currStrategy = allStrat.stream()
                .filter(s -> s.getCategory().getStrategyId().equals(strategyId))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Стратегия не найдена: " + strategyId));
    }

    public void setStrategy(int index) {
        if (index >= 0 && index < allStrat.size()) {
            this.currStrategy = allStrat.get(index);
        }
    }

    @Override
    public void setStrategyByIndex(int index) {
        setStrategy(index);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return currStrategy.calculatePrice(basePrice);
    }

    @Override
    public String getCategoryName() {
        return currStrategy.getCategory().getDisplayName();
    }

    @Override
    public List<PricingStrategy> getStrategies() {
        return allStrat;
    }

    @Override
    public String getMenu() {
        StringBuilder menu = new StringBuilder();
        for (int i = 0; i < allStrat.size(); i++) {
            menu.append((i + 1)).append(". ").append(allStrat.get(i).getCategory().getDisplayName()).append("\n");
        }
        return menu.toString();
    }
}