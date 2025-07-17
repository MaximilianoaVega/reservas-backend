package com.tuempresa.reservas.data;

import com.tuempresa.reservas.model.Role;
import com.tuempresa.reservas.model.Room;
import com.tuempresa.reservas.model.User;
import com.tuempresa.reservas.repository.RoomRepository;
import com.tuempresa.reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            User admin =  User.builder()
                    .email("admin@example.com")
                    .name("root")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            User user = User.builder()
                    .email("user@example.com")
                    .name("John")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build();

            userRepository.saveAll(List.of(admin, user));
        }

        if (roomRepository.count() == 0){
            Room room1 = Room.builder()
                    .name("Sala A")
                    .location("Piso 1")
                    .capacity(10)
                    .available(true)
                    .resources("pizarra")
                    .build();

            Room room2 = Room.builder()
                    .name("Sala B")
                    .location("Piso 2")
                    .capacity(20)
                    .available(true)
                    .resources("proyector")
                    .build();

            roomRepository.saveAll(List.of(room1, room2));
        }

    }
}
