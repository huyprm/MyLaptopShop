package com.ptithcm2021.laptopshop.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class ExportOrderEvent extends ApplicationEvent {
    private final String serialNumber;
    private final Long productDetailId;
    private final Long orderId;

    public ExportOrderEvent(Object source, String serialNumber, Long productDetailId, Long orderId) {
        super(source);
        this.serialNumber = serialNumber;
        this.productDetailId = productDetailId;
        this.orderId = orderId;
    }
}
