package com.ptithcm2021.laptopshop.event;

import com.ptithcm2021.laptopshop.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentSuccessEvent extends ApplicationEvent {

    private final String userId;
    private final Integer amount;
    public PaymentSuccessEvent(Long orderId, String userId, Integer amount) {
        super(orderId);
        this.userId = userId;
        this.amount = amount;
    }

    public Long getOrderId() {
        return (Long) getSource();
    }
}

