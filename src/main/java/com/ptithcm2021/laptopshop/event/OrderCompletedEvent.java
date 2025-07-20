package com.ptithcm2021.laptopshop.event;

import com.ptithcm2021.laptopshop.model.entity.Order;
import org.springframework.context.ApplicationEvent;

public class OrderCompletedEvent extends ApplicationEvent {
    public OrderCompletedEvent(Order order) {
            super(order);
    }

}
