package com.bank.controller;

import com.bank.config.doc.CancelAccountApiCodeStandard;
import com.bank.config.doc.CustomerApiCodeStandard;
import com.bank.controller.request.CustomerRequest;
import com.bank.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    CustomerService customerService;

    @POST
    @Operation(
            summary = "Criar um novo cliente",
            description = "Cria um novo cadastro de cliente com os dados fornecidos no corpo da requisição."
    )
    @CustomerApiCodeStandard()
    public Response createCustomer(CustomerRequest customerRequest) {
        try {
            customerService.createCustomer(customerRequest);
            return Response.status(Response.Status.CREATED).entity("Cadastro criado com sucesso.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            summary = "Cancelar conta de cliente",
            description = "Cancela a conta de um cliente com base no ID fornecido."
    )
    @CancelAccountApiCodeStandard()
    public Response cancelAccount(@PathParam("id") Long id) {
        try {
            customerService.cancelAccount(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
