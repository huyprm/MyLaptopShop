package com.ptithcm2021.laptopshop.model.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    COD("COD"),
    BANK_TRANSFER("Bank Transfer"),
    MOMO("Momo"),
    GOOGLE_PAY("Google Pay"),
    ZALOPAY("ZaloPay");

    private final String value;

    PaymentMethodEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
