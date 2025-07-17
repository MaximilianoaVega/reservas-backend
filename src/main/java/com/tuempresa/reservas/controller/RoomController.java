package com.tuempresa.reservas.controller;

import com.tuempresa.reservas.dto.RoomDTO;
import com.tuempresa.reservas.model.Room;
import com.tuempresa.reservas.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<Room> rooms = service.findAll();
        return ResponseEntity.ok(service.toDTOList(rooms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return service.findById(id)
                .map(service::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(service.save(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return service.findById(id)
                .map(existing -> {
                    room.setId(id);
                    return ResponseEntity.ok(service.save(room));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}