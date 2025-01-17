package com.bank.webhook;

import com.bank.config.doc.DeliveryApiCodeStandard;
import com.bank.service.DeliveryWebhookService;
import com.bank.controller.request.DeliveryRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/webhooks/delivery")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryWebhookController {

    private static final String API_KEY = "transportadora-chave-api";

    @Inject
    DeliveryWebhookService deliveryWebhookService;

    @POST
    @Operation(
            summary = "Processar webhook de entrega",
            description = "Recebe notificações de status de entrega e processa as informações para atualizar o status do cartão."
    )
    @DeliveryApiCodeStandard()
    public Response handleDeliveryWebhook(@HeaderParam("API-KEY") String apiKey,
                                          DeliveryRequest deliveryRequest) {

        if (!API_KEY.equals(apiKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid API key.").build();
        }

        try {
            deliveryWebhookService.handleDeliveryWebhook(deliveryRequest);

        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

        return Response.ok("Delivery webhook processado com successo.").build();
    }

}
