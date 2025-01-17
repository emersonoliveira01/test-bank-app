package com.bank.service;

import com.bank.controller.request.DeliveryRequest;
import com.bank.model.Card;
import com.bank.enums.CardStatus;
import com.bank.enums.CardType;
import com.bank.enums.DeliveryStatus;
import com.bank.repository.CardRepository;
import com.bank.strategy.DeliveryHandlerFactory;
import com.bank.strategy.DeliveryHandlerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryWebhookServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeliveryHandlerFactory deliveryHandlerFactory;

    @Mock
    private DeliveryHandlerStrategy deliveryHandlerStrategy;

    @InjectMocks
    private DeliveryWebhookService deliveryWebhookService;

    private DeliveryRequest deliveryRequest;
    private Card card;

    @BeforeEach
    void setup() {
        deliveryRequest = DeliveryRequest.builder()
                .tracking("123")
                .deliveryStatus(DeliveryStatus.DELIVERED.getDescription())
                .deliveryDate(LocalDateTime.now())
                .deliveryReturnReason(null)
        .build();

        card = Card.builder()
                .id(1L)
                .cardNumber("1234567890123456")
                .cardType(CardType.PHYSICAL)
                .status(CardStatus.INACTIVE)
        .build();
    }

    @Test
    void givenHandleDeliveryWebhook_whenCallDelivery_thenSuccess() {

        when(cardRepository.findByTracking("123")).thenReturn(Optional.of(card));
        when(deliveryHandlerFactory.getStrategy(CardType.PHYSICAL)).thenReturn(deliveryHandlerStrategy);


        doNothing().when(deliveryHandlerStrategy).handleDelivery(card, deliveryRequest);

        deliveryWebhookService.handleDeliveryWebhook(deliveryRequest);

        verify(cardRepository, times(1)).findByTracking("123");
        verify(deliveryHandlerFactory, times(1)).getStrategy(CardType.PHYSICAL);
        verify(deliveryHandlerStrategy, times(1)).handleDelivery(card, deliveryRequest);
    }

    @Test
    void givenHandleDeliveryWebhook_whenCallDelivery_thenCardNotFound() {

        final var expectedMessage = "Cartão não encontrado: 123";

        when(cardRepository.findByTracking("123")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                deliveryWebhookService.handleDeliveryWebhook(deliveryRequest));

        assertEquals(expectedMessage, exception.getMessage());

        verify(cardRepository, times(1)).findByTracking("123");
        verifyNoInteractions(deliveryHandlerFactory);
        verifyNoInteractions(deliveryHandlerStrategy);
    }

    @Test
    void givenHandleDeliveryWebhook_whenCallDelivery_thenDeliveryHandlerStrategyError() {

        final var expectedMessage = "Erro ao processar entrega";

        when(cardRepository.findByTracking("123")).thenReturn(Optional.of(card));
        when(deliveryHandlerFactory.getStrategy(CardType.PHYSICAL)).thenReturn(deliveryHandlerStrategy);

        doThrow(new RuntimeException(expectedMessage)).when(deliveryHandlerStrategy).handleDelivery(card, deliveryRequest);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                deliveryWebhookService.handleDeliveryWebhook(deliveryRequest));

        assertEquals(expectedMessage, exception.getMessage());

        verify(cardRepository, times(1)).findByTracking("123");
        verify(deliveryHandlerFactory, times(1)).getStrategy(CardType.PHYSICAL);
        verify(deliveryHandlerStrategy, times(1)).handleDelivery(card, deliveryRequest);
    }

}