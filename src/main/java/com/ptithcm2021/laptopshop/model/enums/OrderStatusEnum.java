package com.ptithcm2021.laptopshop.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    PENDING("Chờ thanh toán", 1),
    AWAITING("Chờ xử lý", 2),
    PROCESSING("Đang xử lý", 3),
    SHIPPING("Đang giao", 4),
    COMPLETED("Hoàn thành", 5),
    CANCELED("Đã hủy",5);

    private final String value;
    private final int priority;
    OrderStatusEnum(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }
}
