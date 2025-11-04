package com.reservation.service;

import com.reservation.model.Seat;
import com.reservation.model.SeatStatus;
import com.reservation.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsByEvent(Long eventId) {
        return seatRepository.findByEventId(eventId);
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat createSeat(Seat seat) {
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setUpdatedAt(java.time.LocalDateTime.now());
        return seatRepository.save(seat);
    }

    public Seat holdSeat(Long seatId) {
        Optional<Seat> seatOpt = seatRepository.findById(seatId);
        if (seatOpt.isEmpty()) return null;

        Seat seat = seatOpt.get();
        if (seat.getStatus() == SeatStatus.AVAILABLE) {
            seat.setStatus(SeatStatus.HELD);
            seat.setHoldExpiresAt(LocalDateTime.now().plusMinutes(15)); // 15 min hold TTL
            return seatRepository.save(seat);
        }
        return seat;
    }

    public Seat reserveSeat(Long seatId) {
        Optional<Seat> seatOpt = seatRepository.findById(seatId);
        if (seatOpt.isEmpty()) return null;

        Seat seat = seatOpt.get();
        if (seat.getStatus() == SeatStatus.HELD) {
            seat.setStatus(SeatStatus.RESERVED);
            seat.setHoldExpiresAt(null);
            return seatRepository.save(seat);
        }
        return seat;
    }

    public Seat releaseSeat(Long seatId) {
        Optional<Seat> seatOpt = seatRepository.findById(seatId);
        if (seatOpt.isEmpty()) return null;

        Seat seat = seatOpt.get();
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setHoldExpiresAt(null);
        return seatRepository.save(seat);
    }
}
