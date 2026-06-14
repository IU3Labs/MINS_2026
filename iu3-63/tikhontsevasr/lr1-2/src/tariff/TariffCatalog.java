package tariff;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TariffCatalog {
    private final Map<TariffType, TariffStrategy> strategies = new LinkedHashMap<>();

    public TariffCatalog(Collection<TariffStrategy> strategies) {
        for (TariffStrategy strategy : strategies) {
            this.strategies.put(strategy.getType(), strategy);
        }
    }

    public Collection<TariffStrategy> getAll() {
        return strategies.values();
    }
}
