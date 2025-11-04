package com.catalog.service;

import com.catalog.model.Event;
import com.catalog.model.Venue;
import com.catalog.repository.EventRepository;
import com.catalog.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public CatalogService(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    // --- Venue Methods ---
    public Venue createVenue(Venue venue) { return venueRepository.save(venue); }
    public Optional<Venue> getVenueById(Long venueId) { return venueRepository.findById(venueId); }
    public List<Venue> getAllVenues() { return venueRepository.findAll(); }

    public Venue updateVenue(Long venueId, Venue venueDetails) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with ID: " + venueId));

        venue.setName(venueDetails.getName());
        venue.setCity(venueDetails.getCity());
        venue.setCapacity(venueDetails.getCapacity());

        return venueRepository.save(venue);
    }
    public void deleteVenue(Long venueId) { venueRepository.deleteById(venueId); }

    // --- Event Methods ---
    public Event createEvent(Event event) {
        if (event.getVenue() == null || event.getVenue().getVenueId() == null) {
            throw new RuntimeException("Event must have a valid Venue ID");
        }
        Venue venue = venueRepository.findById(event.getVenue().getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found for ID: " + event.getVenue().getVenueId()));
        event.setVenue(venue);
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(Long eventId) { return eventRepository.findById(eventId); }
    public List<Event> getAllEvents() { return eventRepository.findAll(); }
    public List<Event> searchEvents(String city, String type, String status) {
        return eventRepository.searchEvents(city, type, status);
    }

    public Event updateEvent(Long eventId, Event eventDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        event.setTitle(eventDetails.getTitle()); // <-- UPDATED
        event.setEventType(eventDetails.getEventType());
        event.setStatus(eventDetails.getStatus());
        event.setEventDate(eventDetails.getEventDate());
        event.setBasePrice(eventDetails.getBasePrice()); // <-- UPDATED

        return eventRepository.save(event);
    }

    public void deleteEvent(Long eventId) { eventRepository.deleteById(eventId); }

    /**
     * Updates only the status of a specific event.
     * @param eventId The ID of the event to update.
     * @param newStatus The new status (e.g., "CANCELLED", "SOLD_OUT").
     * @return The updated Event object.
     */
    public Event updateEventStatus(Long eventId, String newStatus) {
        // 1. Find the event
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        // 2. Update the status
        event.setStatus(newStatus);

        // 3. Save and return the updated event
        return eventRepository.save(event);
    }
}

