package com.bank.controller;

import com.bank.service.CardService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @Test
    void givenReissuePhysicalCard_whenCallReissueCardIdShouldReturnSuccessResponse() {

        doNothing().when(cardService).reissuePhysicalCard(1L, "Perda");

        var response = cardController.reissuePhysicalCard(1L, "Perda");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Cartão físico reemitido com sucesso.", response.getEntity());

        verify(cardService, times(1)).reissuePhysicalCard(1L, "Perda");
    }

    @Test
    void givenReissuePhysicalCard_whenCallReissueCardIdShouldReturnReturnBadRequestWhenInvalidReason() {

        doThrow(new IllegalArgumentException("Motivo inválido."))
                .when(cardService).reissuePhysicalCard(1L, "Sem motivo");

        var response = cardController.reissuePhysicalCard(1L, "Sem motivo");

        verify(cardService, times(1)).reissuePhysicalCard(1L, "Sem motivo");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}