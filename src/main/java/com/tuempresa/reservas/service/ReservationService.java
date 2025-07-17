package com.tuempresa.reservas.service;

import com.tuempresa.reservas.dto.ReservationDTO;
import com.tuempresa.reservas.model.Reservation;
import com.tuempresa.reservas.model.Room;
import com.tuempresa.reservas.model.User;
import com.tuempresa.reservas.repository.ReservationRepository;
import com.tuempresa.reservas.repository.RoomRepository;
import com.tuempresa.reservas.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAll(){
        return reservationRepository.findAll();
    }

    public List<Reservation> findByUserEmail(String email){
        //escenarion que el usuario no exista
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("user not found"));
        return reservationRepository.findByUser(user);
    };

    public List<ReservationDTO> findMyReservationsAsDto( String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("user not found"));
        return this.toDtoList(reservationRepository.findByUser(user));
    }


    public Reservation create(Long roomId, String userEmail, Reservation reservation){

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new EntityNotFoundException("room not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException("user not found"));
        List<Reservation> overLappingReservations = reservationRepository.findOverlappingReservations(
                room,
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (!overLappingReservations.isEmpty()) {
            throw new IllegalStateException("ya hay una sala reservada en ese horario");
        }

        reservation.setUser(user);
        reservation.setRoom(room);
        return reservationRepository.save(reservation);
    }

    public Reservation update(Long id, Reservation updatedReservation, String name) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("reservation not found"));

        if(!reservation.getUser().getEmail().equals(name)){
            throw new SecurityException("Not autorized");
        }
        reservation.setStartTime(updatedReservation.getStartTime());
        reservation.setEndTime(updatedReservation.getEndTime());
        // se puede cambiar la sala tambien
        return reservationRepository.save(reservation);
    }

    public void delete(Long id, String name) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Reservation not found"));

        if (!reservation.getUser().getEmail().equals(name)) {
            throw new SecurityException("not autorized");
        }
        reservationRepository.delete(reservation);
    }

    public ReservationDTO toDto(Reservation reservation){
        return new ReservationDTO(
                reservation.getId(),
                reservation.getRoom().getName(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );
    }

    public List<ReservationDTO> toDtoList(List<Reservation> reservations){
        return reservations.stream().map(this::toDto).toList();
    }
}
