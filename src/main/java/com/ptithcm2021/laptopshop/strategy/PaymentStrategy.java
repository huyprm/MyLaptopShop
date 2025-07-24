package com.ptithcm2021.laptopshop.strategy;

import com.ptithcm2021.laptopshop.model.dto.request.PaymentRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface PaymentStrategy {
    PaymentResponse pay(PaymentRequest request) throws NoSuchAlgorithmException, InvalidKeyException;
    void handleCallback();
}
