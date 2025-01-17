package com.bank.strategy;

import com.bank.model.Card;
import com.bank.controller.request.DeliveryRequest;

public interface DeliveryHandlerStrategy {
    void handleDelivery(Card card, DeliveryRequest deliveryRequest);
}
