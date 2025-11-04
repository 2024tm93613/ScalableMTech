package com.payment.exception;


public class PaymentAlreadyExistsException extends RuntimeException {
    public PaymentAlreadyExistsException(Long orderId) {
        super("Payment already exists for Order ID: " + orderId);
    }
}

