package com.ptithcm2021.laptopshop.model.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    COD("COD", "Thanh toán khi nhận hàng"),
    BANK_TRANSFER("Bank Transfer", "Chuyển khoản ngân hàng"),
    MOMO("Momo", "Ví điện tử Momo"),
    GOOGLE_PAY("Google Pay", "Ví điện tử Google Pay"),
    ZALOPAY("ZaloPay", "Ví điện tử ZaloPay"),;

    private final String value;
    private final String description;
    PaymentMethodEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }
}
