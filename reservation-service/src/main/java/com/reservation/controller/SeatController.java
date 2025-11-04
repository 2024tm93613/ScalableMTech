package com.reservation.controller;

import com.reservation.model.Seat;
import com.reservation.service.SeatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/seats")
public class SeatController {

    private final SeatService seatService;



    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    public Seat createSeat(@RequestBody Seat seat) {
        return seatService.createSeat(seat);
    }

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/event/{eventId}")
    public List<Seat> getSeatsByEvent(@PathVariable Long eventId) {
        return seatService.getSeatsByEvent(eventId);
    }

    @PostMapping("/{seatId}/hold")
    public Seat holdSeat(@PathVariable Long seatId) {
        return seatService.holdSeat(seatId);
    }

    @PostMapping("/{seatId}/reserve")
    public Seat reserveSeat(@PathVariable Long seatId) {
        return seatService.reserveSeat(seatId);
    }

    @PostMapping("/{seatId}/release")
    public Seat releaseSeat(@PathVariable Long seatId) {
        return seatService.releaseSeat(seatId);
    }
}
