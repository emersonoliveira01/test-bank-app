package com.bank.service;


import com.bank.model.Card;
import com.bank.repository.CardRepository;
import com.bank.strategy.DeliveryHandlerFactory;
import com.bank.controller.request.DeliveryRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class DeliveryWebhookService {

    @Inject
    CardRepository cardRepository;

    @Inject
    DeliveryHandlerFactory deliveryHandlerFactory;

    private static final Logger LOG = Logger.getLogger(String.valueOf(DeliveryWebhookService.class));

    @Transactional
    public void handleDeliveryWebhook(DeliveryRequest deliveryRequest) {

        LOG.info(String.format("Processando confirmação de entrega webhook: %s", deliveryRequest));

        var card = findCardByTracking(deliveryRequest.getTracking());

        var strategy = deliveryHandlerFactory.getStrategy(card.getCardType());
        strategy.handleDelivery(card, deliveryRequest);

        LOG.info(String.format("Entrega processada com sucesso para o cartão %s.", card.getCardNumber()));

    }

    private Card findCardByTracking(String tracking) {
        return cardRepository.findByTracking(tracking)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Cartão não encontrado: %s", tracking)));
    }

}
