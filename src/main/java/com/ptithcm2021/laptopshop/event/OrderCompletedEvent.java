package com.ptithcm2021.laptopshop.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCompletedEvent extends ApplicationEvent {

    private final Integer amount;
    public OrderCompletedEvent(String userId, Integer amount) {
        super(userId);
        this.amount = amount;
    }

    public String getUserId() {
        return (String) getSource();
    }
}
