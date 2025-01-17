package com.bank.webhook;

import com.bank.controller.request.CardProcessorRequest;
import com.bank.service.CardProcessorWebhookService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CardProcessorWebhookControllerTest {

    @Mock
    private CardProcessorWebhookService cardProcessorWebhookService;

    @InjectMocks
    private CardProcessorWebhookController controller;

    private static final String VALID_API_KEY = "processadora-chave-api";
    private static final String INVALID_API_KEY = "invalid-key";

    private CardProcessorRequest cardProcessorRequest;

    @BeforeEach
    void setup() {
        cardProcessorRequest = CardProcessorRequest.builder()
                .cardId("card-123")
                .nextCvv("123")
                .expirationDate(LocalDateTime.now().plusYears(3))
                .build();
    }

    @Test
    void givenHandleCardProcessorWebhook_whenCallCardProcessor_thenSuccess() {

        final var expectedMessage = "Cartão processado webhook com successo.";

        doNothing().when(cardProcessorWebhookService).handleCardProcessorWebhook(cardProcessorRequest);

        var response = controller.handleCardProcessorWebhook(VALID_API_KEY, cardProcessorRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(cardProcessorWebhookService, times(1))
                .handleCardProcessorWebhook(cardProcessorRequest);
    }

    @Test
    void givenHandleCardProcessorWebhook_whenCallCardProcessor_thenInvalidApiKey() {

        final var expectedMessage = "Invalid API key.";

        var response = controller.handleCardProcessorWebhook(INVALID_API_KEY, cardProcessorRequest);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verifyNoInteractions(cardProcessorWebhookService);
    }

    @Test
    void givenHandleCardProcessorWebhook_whenCallCardProcessor_thenCardNotFound() {

        final var expectedMessage = "Cartão não encontrado ID: card-123";

        doThrow(new IllegalArgumentException("Cartão não encontrado ID: card-123"))
                .when(cardProcessorWebhookService)
                .handleCardProcessorWebhook(cardProcessorRequest);

        var response = controller.handleCardProcessorWebhook(VALID_API_KEY, cardProcessorRequest);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(cardProcessorWebhookService, times(1))
                .handleCardProcessorWebhook(cardProcessorRequest);
    }

}