package com.bank.core.model;

import java.util.Locale;

public enum AccountType {
    STANDARD,
    PREMIUM,
    SAVINGS;

    public static AccountType fromString(String value) {
        return AccountType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
