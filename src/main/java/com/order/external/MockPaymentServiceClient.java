package com.order.external;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentServiceClient implements PaymentServiceClient {
    @Override
    public boolean processPayment(Long orderId, double amount) {
        return true; // always succeed for now
    }
}
