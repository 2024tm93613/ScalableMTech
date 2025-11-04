package com.catalog.controller;

import com.catalog.model.Event;
import com.catalog.model.Venue;
import com.catalog.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1") // Using /v1 as per assignment
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // --- Venue Endpoints ---

    @PostMapping("/venues")
    public Venue createVenue(@RequestBody Venue venue) {
        return catalogService.createVenue(venue);
    }

    @GetMapping("/venues")
    public List<Venue> getAllVenues() {
        return catalogService.getAllVenues();
    }

    @GetMapping("/venues/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return catalogService.getVenueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/venues/{id}")
    public Venue updateVenue(@PathVariable Long id, @RequestBody Venue venueDetails) {
        return catalogService.updateVenue(id, venueDetails);
    }

    @DeleteMapping("/venues/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        catalogService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }


    // --- Event Endpoints ---

    @PostMapping("/events")
    public Event createEvent(@RequestBody Event event) {
        return catalogService.createEvent(event);
    }

    /**
     * Searches events by city, type, and status.
     * e.g., /v1/events?city=NewYork&type=CONCERT&status=ON_SALE
     */
    @GetMapping("/events")
    public List<Event> searchEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        return catalogService.searchEvents(city, type, status);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return catalogService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/events/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return catalogService.updateEvent(id, eventDetails);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        catalogService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/events/{id}/status")
    public Event updateEventStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {

        String newStatus = statusUpdate.get("status");
        if (newStatus == null || newStatus.isEmpty()) {
            throw new RuntimeException("New status must be provided in the request body, e.g., {\"status\": \"CANCELLED\"}");
        }
        return catalogService.updateEventStatus(id, newStatus);
    }
}