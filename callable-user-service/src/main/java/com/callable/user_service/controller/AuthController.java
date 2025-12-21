package com.callable.user_service.controller;

import com.callable.user_service.dto.auth.request.LoginGoogleRequestDto;
import com.callable.user_service.dto.auth.request.LoginRequestDto;
import com.callable.user_service.dto.auth.request.RefreshTokenRequestDto;
import com.callable.user_service.dto.auth.request.RegisterRequestDto;
import com.callable.user_service.dto.auth.response.LoginResponseDto;
import com.callable.user_service.dto.auth.response.RefreshTokenResponseDto;
import com.callable.user_service.service.user.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDto registerRequestDto) {
        authService.registerService(registerRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginReponseDto = authService.loginService(loginRequestDto);
        return ResponseEntity.ok(loginReponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshTokenResponseDto refreshTokenResponseDto = authService.refreshTokenService(refreshTokenRequestDto);
        return ResponseEntity.ok(refreshTokenResponseDto);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody LoginGoogleRequestDto dto) {
        return ResponseEntity.ok(authService.loginWithGoogle(dto));
    }
}