package com.callable.user_service.dto.auth.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDto {
    String email;
    String password;
}
