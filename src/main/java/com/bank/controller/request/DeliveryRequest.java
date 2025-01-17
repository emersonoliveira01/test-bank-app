package com.bank.controller.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {
    private String tracking;
    private String deliveryStatus;
    private LocalDateTime deliveryDate;
    private String deliveryReturnReason;
    private String deliveryAddress;
}
