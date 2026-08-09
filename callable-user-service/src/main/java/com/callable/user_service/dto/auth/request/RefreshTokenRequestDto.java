package com.callable.user_service.dto.auth.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequestDto {
    String refreshToken;
}
