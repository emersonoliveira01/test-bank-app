package com.bank.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    DELIVERED("Entregue");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
