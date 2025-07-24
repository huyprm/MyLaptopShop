package com.ptithcm2021.laptopshop.strategy;

import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PaymentStrategyFactory {
    private final Map<PaymentMethodEnum, PaymentStrategy> paymentStrategyMap = new HashMap<>();

    public void register(PaymentMethodEnum method, PaymentStrategy strategy) {
        paymentStrategyMap.put(method, strategy);
    }

    public PaymentStrategy getPaymentStrategy(PaymentMethodEnum method) {
       return Optional.ofNullable(paymentStrategyMap.get(method))
               .orElseThrow(() -> new IllegalArgumentException("Unsupported payment method: " + method));
    }
}
