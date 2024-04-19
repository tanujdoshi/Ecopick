package com.example.backend.dto.response;

import com.example.backend.entities.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}
