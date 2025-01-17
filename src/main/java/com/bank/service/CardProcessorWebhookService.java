package com.bank.service;

import com.bank.enums.CardType;
import com.bank.repository.CardRepository;
import com.bank.controller.request.CardProcessorRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class CardProcessorWebhookService {

    @Inject
    CardRepository cardRepository;

    private static final Logger LOG = Logger.getLogger(String.valueOf(CardProcessorWebhookService.class));

    @Transactional
    public void handleCardProcessorWebhook(@Valid CardProcessorRequest cardProcessorRequest) {

        LOG.info(String.format("Processador de cartão webhook: %s", cardProcessorRequest));

        var card = cardRepository.findByCardId(cardProcessorRequest.getCardId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Cartão não encontrado ID:%s",
                                                                cardProcessorRequest.getCardId())));

        if (Objects.equals(card.getCardType(), CardType.PHYSICAL)) {
            card.setCvv(cardProcessorRequest.getNextCvv());
            card.setExpirationDate(cardProcessorRequest.getExpirationDate());
            cardRepository.persist(card);

            LOG.info(String.format("Cartão: %s CVV updated successo.", card.getCardNumber()));
        }
    }
}
