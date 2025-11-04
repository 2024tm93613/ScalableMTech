package com.catalog.repository;

import com.catalog.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // Find by status
    List<Event> findByStatus(String status);

    // Find by event type
    List<Event> findByEventType(String eventType);

    // Find by venue city
    List<Event> findByVenueCity(String city);

    // Combined search query as required by the assignment
    @Query("SELECT e FROM Event e JOIN e.venue v WHERE " +
            "(:city IS NULL OR v.city = :city) AND " +
            "(:type IS NULL OR e.eventType = :type) AND " +
            "(:status IS NULL OR e.status = :status)")
    List<Event> searchEvents(
            @Param("city") String city,
            @Param("type") String type,
            @Param("status") String status);
}