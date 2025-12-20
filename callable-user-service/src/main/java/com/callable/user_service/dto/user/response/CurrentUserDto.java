package com.callable.user_service.dto.user.response;

import com.callable.user_service.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class CurrentUserDto {
    Long id;
    String fullName;
    String avatar;
    Set<Role> role;
}
