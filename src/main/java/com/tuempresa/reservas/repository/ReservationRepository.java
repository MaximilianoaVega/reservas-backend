package com.tuempresa.reservas.repository;

import com.tuempresa.reservas.model.Reservation;
import com.tuempresa.reservas.model.Room;
import com.tuempresa.reservas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByUser(User user);

    @Query( "SELECT r from Reservation r WHERE r.room = :room AND r.startTime < :end AND  r.endTime  > :start")
    List<Reservation> findOverlappingReservations(
            @Param("room")Room room,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}