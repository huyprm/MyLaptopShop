package com.ptithcm2021.laptopshop.config;

import com.ptithcm2021.laptopshop.strategy.PaymentStrategy;
import com.ptithcm2021.laptopshop.strategy.PaymentStrategyFactory;
import com.ptithcm2021.laptopshop.validator.PaymentHandler;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyRegistrar implements BeanPostProcessor {
    private final PaymentStrategyFactory factory;

    public PaymentStrategyRegistrar(PaymentStrategyFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof PaymentStrategy) {
            PaymentHandler annotation = bean.getClass().getAnnotation(PaymentHandler.class);
            if (annotation != null) {
                factory.register(annotation.value(), (PaymentStrategy) bean);
            }
        }
        return bean;
    }
}
