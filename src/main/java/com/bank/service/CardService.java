package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Card;
import com.bank.enums.CardStatus;
import com.bank.enums.CardType;
import com.bank.enums.DeliveryStatus;
import com.bank.enums.Reason;
import com.bank.repository.CardRepository;

import com.bank.controller.request.DeliveryRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class CardService {

    private static final Logger LOG =
            Logger.getLogger(String.valueOf(CardService.class));

    @Inject
    CardRepository cardRepository;

    public void creteCard(Account account, CardType cardType) {
        var card = Card.with(account, cardType);
        save(card);
    }

    @Transactional
    public void reissuePhysicalCard(Long cardId, String reason) {
        Card card = cardRepository.findById(cardId);

        if (Objects.isNull(card) || card.getCardType() != CardType.PHYSICAL) {
            throw new IllegalArgumentException("Cartão fisico não encontrado.");
        }

        if (!Reason.isReasonAuthorization(reason)) {
            throw new IllegalArgumentException(String.format("Motivo: %s, não authorizado para reemissão do cartão.", reason));
        }

        card.setStatus(CardStatus.INACTIVE);
        save(card);

        creteCard(card.getAccount(), CardType.PHYSICAL);
    }

    public void checkIfCardWasDelivered(Card card, DeliveryRequest delivery) {
        if (Objects.equals(DeliveryStatus.DELIVERED.getDescription(), delivery.getDeliveryStatus())) {

            card.setStatus(CardStatus.ACTIVE);
            card.setDeliveryDate(delivery.getDeliveryDate());
            card.setDeliveryReturnReason(delivery.getDeliveryReturnReason());
            card.setDeliveryAddress(delivery.getDeliveryAddress());
            save(card);
            createCardVirtual(card);

            LOG.info(String.format("Cartão %s foi entregue e ativado com sucesso.", card.getCardNumber()));

        } else {
            LOG.warning(String.format(
                    "Falha na entrega do cartão %s. Status da entrega: %s. Motivo: %s",
                    card.getCardNumber(),
                    delivery.getDeliveryStatus(),
                    delivery.getDeliveryReturnReason()));

            throw new IllegalArgumentException(String.format(
                    "Cartão %s não pode ser ativado devido ao status de entrega: %s",
                    card.getCardNumber(),
                    delivery.getDeliveryStatus()));
        }
    }

    private void save(Card card) {
        cardRepository.persist(card);
    }

    private void createCardVirtual(Card card) {
        creteCard(card.getAccount(), CardType.VIRTUAL);
        LOG.info(String.format("Cartão virtual criado para a conta %s.", card.getAccount().getAccountNumber()));
    }
}
