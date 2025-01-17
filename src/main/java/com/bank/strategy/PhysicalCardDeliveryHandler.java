package com.bank.strategy;

import com.bank.model.Card;
import com.bank.enums.CardStatus;
import com.bank.service.CardService;
import com.bank.controller.request.DeliveryRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class PhysicalCardDeliveryHandler implements DeliveryHandlerStrategy {

    private static final Logger LOG =
            Logger.getLogger(String.valueOf(PhysicalCardDeliveryHandler.class));

    @Inject
    CardService cardService;

    @Override
    public void handleDelivery(Card card, DeliveryRequest deliveryRequest) {

        if (Objects.equals(card.getStatus(), CardStatus.ACTIVE)) {
            throw new IllegalArgumentException(String.format("Cartão %s já está ativado.", card.getCardNumber()));
        }

        cardService.checkIfCardWasDelivered(card, deliveryRequest);
    }
}
