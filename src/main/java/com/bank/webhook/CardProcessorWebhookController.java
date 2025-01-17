package com.bank.webhook;

import com.bank.config.doc.CardProcessorApiCodeStandard;
import com.bank.service.CardProcessorWebhookService;
import com.bank.controller.request.CardProcessorRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/webhooks/card-processor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardProcessorWebhookController {

    @Inject
    CardProcessorWebhookService cardProcessorWebhookService;

    private static final String API_KEY = "processadora-chave-api";

    @POST
    @Operation(
            summary = "Processar webhook da processadora de cartões",
            description = "Recebe um webhook da processadora de cartões para atualizar as informações do cartão."
    )
    @CardProcessorApiCodeStandard()
    public Response handleCardProcessorWebhook(@HeaderParam("API-KEY") String apiKey,
                                               @Valid CardProcessorRequest cardProcessorRequest) {

        if (!API_KEY.equals(apiKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid API key.").build();
        }

        try {
            cardProcessorWebhookService.handleCardProcessorWebhook(cardProcessorRequest);

        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

        return Response.ok("Cartão processado webhook com successo.").build();
    }
}
