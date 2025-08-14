package com.ptithcm2021.laptopshop.event;

import com.ptithcm2021.laptopshop.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentSuccessEvent extends ApplicationEvent {

    public PaymentSuccessEvent(Long orderId) {
        super(orderId);
    }

    public Long getOrderId() {
        return (Long) getSource();
    }
}

