package com.bank.config.doc;

import jakarta.ws.rs.NameBinding;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@APIResponses({
        @APIResponse(
                responseCode = "204",
                description = "Conta cancelada com sucesso."
        ),
        @APIResponse(
                responseCode = "404",
                description = "Cliente não encontrado.",
                content = @Content(mediaType = MediaType.TEXT_PLAIN)
        )
})
public @interface CancelAccountApiCodeStandard {
}
