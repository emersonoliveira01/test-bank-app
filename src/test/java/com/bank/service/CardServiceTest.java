package com.bank.service;

import com.bank.controller.request.DeliveryRequest;
import com.bank.model.Account;
import com.bank.model.Card;
import com.bank.enums.CardStatus;
import com.bank.enums.CardType;
import com.bank.enums.DeliveryStatus;
import com.bank.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Account account;
    private Card card;
    private DeliveryRequest deliveryRequest;

    @BeforeEach
    void setup() {
        account = Account.builder()
                .id(1L)
                .accountNumber("123456")
                .build();

        card = Card.builder()
                .id(1L)
                .cardType(CardType.PHYSICAL)
                .cardNumber("CARD-00000051515")
                .account(account)
        .build();

        deliveryRequest = DeliveryRequest.builder()
                .tracking("123456")
                .deliveryStatus(DeliveryStatus.DELIVERED.getDescription())
                .deliveryDate(LocalDateTime.now())
                .deliveryReturnReason(null)
                .deliveryAddress("Rua teste")
        .build();
    }

    @Test
    void givenCreteCard_whenCreatePhysicalCard_thenCreteCard_ShouldPersistNewCard() {

        cardService.creteCard(account, CardType.PHYSICAL);

        verify(cardRepository, times(1)).persist(any(Card.class));
    }

    @Test
    void givenReissuePhysicalCard_ShouldReissueCardSuccessfully() {
        when(cardRepository.findById(1L)).thenReturn(card);

        cardService.reissuePhysicalCard(1L, "Perda");

        verify(cardRepository, times(2)).persist(any(Card.class));
    }

    @Test
    void givenReissuePhysicalCard_ShouldThrowExceptionIfCardNotPhysical() {

        final var expectedMessage = "Cartão fisico não encontrado.";

        card.setCardType(CardType.VIRTUAL);
        when(cardRepository.findById(1L)).thenReturn(card);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cardService.reissuePhysicalCard(1L, "Perda"));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void givenReissuePhysicalCard_ShouldThrowExceptionIfReasonNotAuthorized() {

        final var expectedMessage = "Motivo: Sem motivo, não authorizado para reemissão do cartão.";

        when(cardRepository.findById(1L)).thenReturn(card);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cardService.reissuePhysicalCard(1L, "Sem motivo"));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void givenCheckIfCardWasDelivered_ShouldActivateAndCreateVirtualCard() {

        cardService.checkIfCardWasDelivered(card, deliveryRequest);

        assertEquals(CardStatus.ACTIVE, card.getStatus());
        verify(cardRepository, times(1)).persist(card);
    }

    @Test
    void  givenCheckIfCardWasDelivered_ShouldThrowExceptionIfDeliveryFailed() {

        final var expectedMessage = "Cartão CARD-00000051515 não pode ser ativado devido ao status de entrega: Falha";

        deliveryRequest.setDeliveryStatus("Falha");
        deliveryRequest.setDeliveryReturnReason("Endereço inválido");

        var exception = assertThrows(IllegalArgumentException.class,
                () -> cardService.checkIfCardWasDelivered(card, deliveryRequest));

        assertEquals(expectedMessage, exception.getMessage());
        verify(cardRepository, never()).persist(any(Card.class));
    }

}