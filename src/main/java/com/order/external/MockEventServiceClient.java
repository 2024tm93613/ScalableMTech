package com.order.external;

import org.springframework.stereotype.Service;

@Service
public class MockEventServiceClient implements EventServiceClient {
    @Override
    public boolean isEventAvailable(Long eventId) {
        return eventId != null && eventId < 100; // all events with ID < 100 exist
    }
}
