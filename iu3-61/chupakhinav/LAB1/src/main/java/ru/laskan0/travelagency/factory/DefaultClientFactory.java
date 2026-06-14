package ru.laskan0.travelagency.factory;

import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Discount;

public class DefaultClientFactory extends ClientFactory {
    @Override
    protected Client createClient(String id, String fullName, String phone, Discount discount) {
        return new Client(id, fullName, phone, discount);
    }
}
