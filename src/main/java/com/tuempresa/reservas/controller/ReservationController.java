package com.tuempresa.reservas.controller;


import com.tuempresa.reservas.dto.ReservationDTO;
import com.tuempresa.reservas.dto.RoomDTO;
import com.tuempresa.reservas.model.Reservation;
import com.tuempresa.reservas.model.Room;
import com.tuempresa.reservas.service.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<Reservation>> getAll(){
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationDTO>> getMyReservations(Principal principal){
        List<ReservationDTO> myReservations = reservationService.findMyReservationsAsDto(principal.getName());
        return ResponseEntity.ok(myReservations);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestParam Long roomId,
            @RequestBody Reservation reservation,
            Principal principal
    ){
        try {
            return ResponseEntity.ok(
                    reservationService.create(roomId, principal.getName(), reservation)
            );
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation updatedReservation,
            Principal principal
    ){
        try {
            Reservation result = reservationService.update(id, updatedReservation,principal.getName() );
            return ResponseEntity.ok(result);
        }catch (SecurityException e){
            return ResponseEntity.status(403).body("no tienes permiso para modificar esta reserva");
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(
            @PathVariable Long id,
            Principal principal
    ){
        try {
            reservationService.delete(id, principal.getName());
            return ResponseEntity.noContent().build();
        }catch (SecurityException e){
            return ResponseEntity.status(403).body("no tienes permiso para modificar esta reserva");
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
