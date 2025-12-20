package com.callable.user_service.service.user;


import com.callable.user_service.dto.common.UserAuthDto;
import com.callable.user_service.dto.user.request.LoginGoogleRequestDto;
import com.callable.user_service.dto.user.request.LoginRequestDto;
import com.callable.user_service.dto.user.request.RegisterRequestDto;
import com.callable.user_service.dto.user.response.LoginResponseDto;
import com.callable.user_service.enums.AuthProvider;
import com.callable.user_service.enums.Role;
import com.callable.user_service.model.Users;
import com.callable.user_service.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    JWTService jwtService;

    AuthenticationManager authManager;

    UserRepository userRepository;

    GoogleTokenVerifierService googleTokenVerifierService;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public LoginResponseDto loginService(LoginRequestDto loginRequestDto) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        if (!authentication.isAuthenticated())
            throw new UsernameNotFoundException(loginRequestDto.getEmail());

        Users user = (Users) authentication.getPrincipal();

        return buildLoginResponse(user);
    }

    public LoginResponseDto loginWithGoogle(LoginGoogleRequestDto dto) {

        GoogleIdToken.Payload payload = googleTokenVerifierService.verify(dto.getIdToken());

        String email = payload.getEmail();

        Users user = userRepository.findByEmail(email)
                .orElseGet(() -> createGoogleUser(payload));

        return buildLoginResponse(user);
    }


    public void registerService(RegisterRequestDto registerRequestDto) {
        Users user = new Users();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(encoder.encode(registerRequestDto.getPassword()));
        user.setRole(Set.of(Role.ROLE_USER));
        userRepository.save(user);
    }

    private Users createGoogleUser(GoogleIdToken.Payload payload) {

        Users user = new Users();
        user.setEmail(payload.getEmail());
        user.setPassword("");
        user.setRole(Set.of(Role.ROLE_USER));
        user.setProvider(AuthProvider.GOOGLE);

        return userRepository.save(user);
    }

    private LoginResponseDto buildLoginResponse(Users user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return LoginResponseDto.builder()
                .user(UserAuthDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .avatar(user.getAvatar())
                        .build())
                .accessToken(accessToken)
                .accessTokenExpiresAt(jwtService.getAccessTokenExpirationTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(jwtService.getRefreshTokenExpirationTime())
                .build();
    }

}