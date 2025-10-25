package com.order.external;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class MockSeatingServiceClient implements SeatingServiceClient {
    @Override
    public List<Long> getAvailableSeats(Long eventId, int numSeats) {
        return LongStream.range(1, numSeats + 1).boxed().collect(Collectors.toList());
    }

    @Override
    public double getSeatPrice(Long eventId, Long seatId) {
        return 100.0 + seatId % 10 * 10;
    }
}
