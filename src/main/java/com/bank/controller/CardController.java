package com.bank.controller;

import com.bank.config.doc.CardApiCodeStandard;
import com.bank.service.CardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/cards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardController  {

    @Inject
    CardService cardService;

    @POST
    @Path("/reissue/{cardId}")
    @Operation(
            summary = "Reemissão de cartão físico",
            description = "Reemite um cartão físico para um cliente, com base no ID do cartão e no motivo fornecido."
    )
    @CardApiCodeStandard()
    public Response reissuePhysicalCard(@PathParam("cardId") Long cardId,
                                        @QueryParam("reason") String reason) {

        try {
            cardService.reissuePhysicalCard(cardId, reason);
            return Response.ok("Cartão físico reemitido com sucesso.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
