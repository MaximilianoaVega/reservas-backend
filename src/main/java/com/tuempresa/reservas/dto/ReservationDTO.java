package com.tuempresa.reservas.dto;
import java.time.LocalDateTime;


public record ReservationDTO(
        Long reservationId,
        String roomName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {

}
