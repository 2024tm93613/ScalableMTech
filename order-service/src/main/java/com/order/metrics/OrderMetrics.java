package com.order.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Custom Prometheus metrics for Order Service
 * Mirrors order totals and seat reservation failures
 */
@Component
public class OrderMetrics {

    private final Counter ordersTotal;
    private final Counter seatReservationsFailed;

    public OrderMetrics(MeterRegistry registry) {
        this.ordersTotal = Counter.builder("orders_total")
                .description("Total number of successful orders processed")
                .register(registry);

        this.seatReservationsFailed = Counter.builder("seat_reservations_failed")
                .description("Total seat reservation failures")
                .register(registry);
    }

    /** Increment on successful order */
    public void incrementOrder() {
        ordersTotal.increment();
    }

    /** Increment on failed seat reservation */
    public void incrementSeatReservationFailure() {
        seatReservationsFailed.increment();
    }
}
