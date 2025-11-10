package com.payment.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Custom Prometheus metrics for Payment Service
 */
@Component
public class PaymentMetrics {

    private final Counter paymentsTotal;
    private final Counter paymentsFailedTotal;
    private final Counter paymentsAmountTotal;

    public PaymentMetrics(MeterRegistry registry) {
        this.paymentsTotal = Counter.builder("payment_total")
                .description("Total number of successful payments processed")
                .register(registry);

        this.paymentsFailedTotal = Counter.builder("payments_failed_total")
                .description("Total number of failed payment attempts")
                .register(registry);

        this.paymentsAmountTotal = Counter.builder("payments_amount_total")
                .description("Total amount of all successful payments")
                .register(registry);
    }

    /** Increment on successful payment */
    public void incrementSuccess(double amount) {
        paymentsTotal.increment();
        paymentsAmountTotal.increment(amount);
    }

    /** Increment on failed payment */
    public void incrementFailure() {
        paymentsFailedTotal.increment();
    }

    /** Add custom amount metric separately if needed */
    public void addAmount(double amount) {
        paymentsAmountTotal.increment(amount);
    }
}
