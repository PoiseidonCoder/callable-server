package com.callable.user_service.dto.common;

import com.callable.user_service.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserAuthDto {
    Long id;
    String email;
    Set<Role> roles;
}
