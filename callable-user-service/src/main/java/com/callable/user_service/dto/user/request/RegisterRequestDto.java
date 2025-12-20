package com.callable.user_service.dto.user.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDto {
    String email;
    String password;
}
