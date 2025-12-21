package com.callable.user_service.service.user;

import com.callable.user_service.dto.user.response.CurrentUserDto;
import com.callable.user_service.model.Users;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    public CurrentUserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        return CurrentUserDto.builder()
                .id(user.getId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .build();
    }

}
