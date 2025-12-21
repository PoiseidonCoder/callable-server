package com.callable.user_service.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponseDto {
    String accessToken;
    String refreshToken;
    Long accessTokenExpiresAt;
}
