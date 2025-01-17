package com.bank.service;

import com.bank.controller.request.CardProcessorRequest;
import com.bank.model.Card;
import com.bank.enums.CardType;
import com.bank.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardProcessorWebhookServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardProcessorWebhookService service;

    private Card card;
    private CardProcessorRequest cardProcessorRequest;

    @BeforeEach
    void setup() {
        card = new Card();
        card.setId(1L);
        card.setCardType(CardType.PHYSICAL);
        card.setCardNumber("1234567890123456");
        card.setCvv("111");
        card.setExpirationDate(LocalDateTime.now().plusYears(2));

        cardProcessorRequest = new CardProcessorRequest();
        cardProcessorRequest.setCardId("1");
        cardProcessorRequest.setNextCvv("222");
        cardProcessorRequest.setExpirationDate(LocalDateTime.now().plusYears(3));
    }

    @Test
    void givenHandleCardProcessorWebhook_whenCallCardProcessor_thenCardUpdatedSuccessfully() {

        final var expectedCVV = "222";

        when(cardRepository.findByCardId(anyString())).thenReturn(Optional.of(card));

        service.handleCardProcessorWebhook(cardProcessorRequest);

        assertEquals(expectedCVV, card.getCvv());
        assertEquals(cardProcessorRequest.getExpirationDate(), card.getExpirationDate());

        verify(cardRepository, times(1)).persist(card);
    }

    @Test
    void givenHandleCardProcessorWebhook_whenCallCardProcessor_thenCardNotFound() {

        final var expectedMessage = "Cartão não encontrado ID:1";

        when(cardRepository.findByCardId(anyString())).thenReturn(Optional.empty());

        final var exception = assertThrows(IllegalArgumentException.class,
                () -> service.handleCardProcessorWebhook(cardProcessorRequest));

        assertEquals(expectedMessage, exception.getMessage());
        verify(cardRepository, never()).persist(any(Card.class));
    }

}