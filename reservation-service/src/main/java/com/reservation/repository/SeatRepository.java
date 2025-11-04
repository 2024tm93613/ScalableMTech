package com.reservation.repository;

import com.reservation.model.Seat;
import com.reservation.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByEventId(Long eventId);
    List<Seat> findByStatus(SeatStatus status);

}
