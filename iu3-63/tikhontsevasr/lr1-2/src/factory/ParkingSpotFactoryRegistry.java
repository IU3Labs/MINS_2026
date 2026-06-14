package factory;

import exception.InvalidOperationException;
import model.SpotType;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class ParkingSpotFactoryRegistry {
    private final Map<SpotType, ParkingSpotFactory> factories = new EnumMap<>(SpotType.class);

    public ParkingSpotFactoryRegistry(Collection<ParkingSpotFactory> factories) {
        for (ParkingSpotFactory factory : factories) {
            this.factories.put(factory.getSupportedType(), factory);
        }
    }

    public ParkingSpotFactory getFactory(SpotType type) {
        ParkingSpotFactory factory = factories.get(type);
        if (factory == null) {
            throw new InvalidOperationException("Для типа места " + type.getDisplayName() + " фабрика не зарегистрирована.");
        }
        return factory;
    }
}
