package com.tuempresa.reservas.dto;

public record RoomDTO(
        Long id,
        String name,
        String location,
        Integer capacity
) {
}
