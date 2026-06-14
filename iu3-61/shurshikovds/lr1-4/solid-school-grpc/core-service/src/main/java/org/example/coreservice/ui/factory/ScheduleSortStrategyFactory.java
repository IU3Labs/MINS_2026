package org.example.coreservice.ui.factory;

import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.strategy.ScheduleSortStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScheduleSortStrategyFactory {

    private final Map<Integer, ScheduleSortStrategy> strategies;

    public ScheduleSortStrategyFactory(List<ScheduleSortStrategy> strategies) {
        int[] key = {1};
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ScheduleSortStrategy::key, c -> c));
    }

    public ScheduleSortStrategy ask(InputUtil inputUtil) {
        System.out.println("\nСортировка:");
        strategies.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
        System.out.println("0. Выход");

        ScheduleSortStrategy strategy = strategies.get(inputUtil.readInt("Выбор: "));
        if (strategy == null) {
            System.out.println("Неверный выбор, применяется сортировка по умолчанию");
            return strategies.get(1);
        }
        return strategy;
    }
}

