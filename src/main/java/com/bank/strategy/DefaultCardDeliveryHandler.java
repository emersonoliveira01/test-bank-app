package com.bank.strategy;

import com.bank.model.Card;
import com.bank.controller.request.DeliveryRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultCardDeliveryHandler implements DeliveryHandlerStrategy {

    @Override
    public void handleDelivery(Card card, DeliveryRequest deliveryRequest) {
        throw new UnsupportedOperationException("Entrega não suportada para este tipo de cartão.");
    }
}
