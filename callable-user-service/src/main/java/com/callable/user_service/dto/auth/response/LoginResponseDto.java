package com.callable.user_service.dto.auth.response;

import com.callable.user_service.dto.common.UserAuthDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    UserAuthDto user;
    String accessToken;
    Long accessTokenExpiresTime;
    String refreshToken;
    Long refreshTokenExpiresTime;
}
