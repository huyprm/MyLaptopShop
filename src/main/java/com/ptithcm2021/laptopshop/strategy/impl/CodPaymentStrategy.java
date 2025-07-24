package com.ptithcm2021.laptopshop.strategy.impl;

import com.ptithcm2021.laptopshop.model.dto.request.PaymentRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import com.ptithcm2021.laptopshop.strategy.PaymentStrategy;
import com.ptithcm2021.laptopshop.validator.PaymentHandler;
import org.springframework.stereotype.Component;

@PaymentHandler(PaymentMethodEnum.COD)
@Component
public class CodPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResponse pay(PaymentRequest request) {
        return null;
    }

    @Override
    public void handleCallback() {

    }
}
