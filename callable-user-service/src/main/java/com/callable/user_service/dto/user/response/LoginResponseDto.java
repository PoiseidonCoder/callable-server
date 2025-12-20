package com.callable.user_service.dto.user.response;

import com.callable.user_service.dto.common.UserAuthDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    UserAuthDto user;
    String accessToken;
    Long accessTokenExpiresAt;
    String refreshToken;
    Long refreshTokenExpiresAt;
}
