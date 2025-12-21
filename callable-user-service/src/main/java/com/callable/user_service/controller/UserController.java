package com.callable.user_service.controller;

import com.callable.user_service.dto.auth.response.CurrentUserDto;
import com.callable.user_service.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        CurrentUserDto currentUserDto = userService.getCurrentUser();
        return ResponseEntity.ok(currentUserDto);
    }

}
