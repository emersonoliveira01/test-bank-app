package com.bank.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardProcessorRequest {
    private String accountId;
    private String cardId;
    private String nextCvv;
    private LocalDateTime expirationDate;

}
