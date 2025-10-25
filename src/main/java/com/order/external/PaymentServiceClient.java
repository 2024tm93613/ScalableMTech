package com.order.external;

public interface PaymentServiceClient {
    boolean processPayment(Long orderId, double amount);
}
