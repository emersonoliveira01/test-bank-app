package com.bank.webhook;

import com.bank.controller.request.DeliveryRequest;
import com.bank.enums.DeliveryStatus;
import com.bank.service.DeliveryWebhookService;
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
class DeliveryWebhookControllerTest {

    @Mock
    private DeliveryWebhookService deliveryWebhookService;

    @InjectMocks
    private DeliveryWebhookController controller;

    private static final String VALID_API_KEY = "transportadora-chave-api";
    private static final String INVALID_API_KEY = "invalid-api-key";

    private DeliveryRequest deliveryRequest;

    @BeforeEach
    void setup() {

        deliveryRequest = DeliveryRequest.builder()
                .tracking("tracking-123")
                .deliveryStatus(DeliveryStatus.DELIVERED.getDescription())
                .deliveryDate(LocalDateTime.now())
                .deliveryReturnReason("Local de teste")
                .deliveryAddress("Rua teste, 1")
                .build();
    }

    @Test
    void givenHandleDeliveryWebhook_whenCallDelivery_thenSuccess() {

        final var expectedMessage = "Delivery webhook processado com successo.";

        doNothing().when(deliveryWebhookService).handleDeliveryWebhook(deliveryRequest);

        var response = controller.handleDeliveryWebhook(VALID_API_KEY, deliveryRequest);

              assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(deliveryWebhookService, times(1)).handleDeliveryWebhook(deliveryRequest);
    }

    @Test
    void givenHandleDeliveryWebhook_whenCallDelivery_thenInvalidApiKey() {

        final var expectedMessage = "Invalid API key.";

        var response = controller.handleDeliveryWebhook(INVALID_API_KEY, deliveryRequest);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verifyNoInteractions(deliveryWebhookService);
    }

    @Test
    void givenHandleDeliveryWebhook_whenCallDelivery_thenDeliveryNotFound() {

        final var expectedMessage = "Delivery não encontrado: tracking-123";

        doThrow(new IllegalArgumentException("Delivery não encontrado: tracking-123"))
                .when(deliveryWebhookService)
                .handleDeliveryWebhook(deliveryRequest);

        var response = controller.handleDeliveryWebhook(VALID_API_KEY, deliveryRequest);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(deliveryWebhookService, times(1)).handleDeliveryWebhook(deliveryRequest);
    }

}