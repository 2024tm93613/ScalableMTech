package com.order.external;

import java.util.List;

public interface SeatingServiceClient {
    List<Long> getAvailableSeats(Long eventId, int numSeats);
    double getSeatPrice(Long eventId, Long seatId);
}
