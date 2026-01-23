package com.store.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
