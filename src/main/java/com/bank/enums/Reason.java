package com.bank.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum Reason {
    LOSS ("Perda"),
    THEFT("Roubo");

    private final String description;

    Reason(String description) {
        this.description = description;
    }

    public static boolean isReasonAuthorization(String reason) {
        return Arrays.stream(values())
                .anyMatch(match -> Objects.equals(match.getDescription(), reason));
    }
}
