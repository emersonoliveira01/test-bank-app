package com.bank.strategy;

import com.bank.enums.CardType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeliveryHandlerFactory {

    @Inject
    PhysicalCardDeliveryHandler physicalCardDeliveryHandler;

    @Inject
    DefaultCardDeliveryHandler defaultCardDeliveryHandler;

    public DeliveryHandlerStrategy getStrategy(CardType cardType) {
        if (cardType == CardType.PHYSICAL) {
            return physicalCardDeliveryHandler;
        }
        return defaultCardDeliveryHandler;
    }
}
