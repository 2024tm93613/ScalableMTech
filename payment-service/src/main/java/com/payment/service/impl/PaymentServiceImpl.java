package com.payment.service.impl;

import com.payment.exception.PaymentAlreadyExistsException;
import com.payment.model.Payment;
import com.payment.repository.PaymentRepository;
import com.payment.service.PaymentService;
import com.payment.metrics.PaymentMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMetrics paymentMetrics;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment createPayment(Payment payment) {
        try {
            if (paymentRepository.existsByOrderId(payment.getOrderId())) {
                throw new PaymentAlreadyExistsException(payment.getOrderId());
            }

            Payment newPayment = new Payment();
            newPayment.setOrderId(payment.getOrderId());
            newPayment.setAmount(payment.getAmount());
            newPayment.setMethod(payment.getMethod());

            Payment savedPayment = paymentRepository.save(newPayment);

            // ✅ Update metrics for successful payment
            paymentMetrics.incrementSuccess(savedPayment.getAmount());
            return savedPayment;

        } catch (Exception e) {
            // ✅ Count failed attempts and simulate seat reservation failures
            paymentMetrics.incrementFailure();
            throw e;
        }
    }

    @Override
    public Payment updatePayment(Long id, Payment paymentDetails) {
        return paymentRepository.findById(id)
                .map(payment -> {
                    payment.setOrderId(paymentDetails.getOrderId());
                    payment.setAmount(paymentDetails.getAmount());
                    payment.setMethod(paymentDetails.getMethod());
                    payment.setStatus(paymentDetails.getStatus());
                    payment.setReference(paymentDetails.getReference());
                    payment.setCreatedAt(paymentDetails.getCreatedAt());
                    return paymentRepository.save(payment);
                })
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
