package com.reservation.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Custom Prometheus metrics for Reservation Service
 * Tracks successful and failed seat reservations
 */
@Component
public class ReservationMetrics {

    private final Counter reservationsTotal;
    private final Counter seatReservationsFailed;

    public ReservationMetrics(MeterRegistry registry) {
        this.reservationsTotal = Counter.builder("reservations_total")
                .description("Total number of successful seat reservations")
                .register(registry);

        this.seatReservationsFailed = Counter.builder("seat_reservations_failed")
                .description("Total number of failed seat reservations")
                .register(registry);
    }

    /** Increment on successful reservation */
    public void incrementSuccess() {
        reservationsTotal.increment();
    }

    /** Increment on failed reservation */
    public void incrementFailure() {
        seatReservationsFailed.increment();
    }

    /** Increment by a custom count (optional) */
    public void incrementFailureBy(double count) {
        seatReservationsFailed.increment(count);
    }
}
