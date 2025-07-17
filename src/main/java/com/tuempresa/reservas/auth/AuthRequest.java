package com.tuempresa.reservas.auth;

import com.tuempresa.reservas.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthRequest {

    private String email;
    private String Password;
    private Role role;

}
