package com.callable.user_service.dto.user.request;

import com.callable.user_service.enums.AuthProvider;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginGoogleRequestDto {
    private AuthProvider provider;
    private String idToken;
}
