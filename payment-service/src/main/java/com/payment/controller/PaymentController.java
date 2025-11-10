package com.payment.controller;

import com.payment.model.Payment;
import com.payment.service.PaymentService;
import com.payment.metrics.PaymentMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final PaymentMetrics paymentMetrics;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentMetrics paymentMetrics) {
        this.paymentService = paymentService;
        this.paymentMetrics = paymentMetrics;
    }

    // Generate or reuse correlation ID
    private String getOrGenerateCorrelationId(String correlationId) {
        return (correlationId != null && !correlationId.isEmpty())
                ? correlationId
                : UUID.randomUUID().toString();
    }

    @GetMapping
    public List<Payment> getAllPayments(@RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        String cid = getOrGenerateCorrelationId(correlationId);
        logger.info("[{}] Fetching all payments", cid);
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id,
                                                  @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        String cid = getOrGenerateCorrelationId(correlationId);
        logger.info("[{}] Fetching payment ID {}", cid, id);

        Optional<Payment> paymentOpt = paymentService.getPaymentById(id);
        return paymentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("[{}] Payment not found with ID {}", cid, id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment,
                                           @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        String cid = getOrGenerateCorrelationId(correlationId);
        logger.info("[{}] Creating payment for order {}", cid, payment.getOrderId());

        try {
            Payment savedPayment = paymentService.createPayment(payment);

            logger.info("[{}] Payment created successfully: ID={} Amount={}",
                    cid, savedPayment.getPaymentId(), savedPayment.getAmount());

            return ResponseEntity.ok(savedPayment);
        } catch (Exception e) {
            logger.error("[{}] Payment creation failed: {}", cid, e.getMessage());
            return ResponseEntity.internalServerError().body("Payment failed: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Long id,
                                           @RequestBody Payment paymentDetails,
                                           @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        String cid = getOrGenerateCorrelationId(correlationId);
        logger.info("[{}] Updating payment ID {}", cid, id);

        try {
            Payment updatedPayment = paymentService.updatePayment(id, paymentDetails);
            logger.info("[{}] Payment updated successfully for ID {}", cid, id);
            return ResponseEntity.ok(updatedPayment);
        } catch (Exception e) {
            logger.error("[{}] Payment update failed for ID {}: {}", cid, id, e.getMessage());
            return ResponseEntity.internalServerError().body("Payment update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id,
                                           @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        String cid = getOrGenerateCorrelationId(correlationId);
        logger.info("[{}] Deleting payment ID {}", cid, id);

        try {
            paymentService.deletePayment(id);
            logger.info("[{}] Payment deleted successfully for ID {}", cid, id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("[{}] Failed to delete payment {}: {}", cid, id, e.getMessage());
            return ResponseEntity.internalServerError().body("Payment deletion failed: " + e.getMessage());
        }
    }
}
