package com.tuempresa.reservas.repository;

import com.tuempresa.reservas.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}