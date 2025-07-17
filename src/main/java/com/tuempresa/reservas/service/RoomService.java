package com.tuempresa.reservas.service;

import com.tuempresa.reservas.dto.RoomDTO;
import com.tuempresa.reservas.model.Room;
import com.tuempresa.reservas.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public List<Room> findAll() {
        return repository.findAll();
    }

    public Optional<Room> findById(Long id) {
        return repository.findById(id);
    }

    public Room save(Room room) {
        return repository.save(room);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public RoomDTO toDTO(Room room){
        return new RoomDTO(
                room.getId(),
                room.getName(),
                room.getLocation(),
                room.getCapacity()
        );
    }
    public List<RoomDTO> toDTOList( List<Room> rooms){
        return rooms.stream().map(this::toDTO).toList();
    }
}
