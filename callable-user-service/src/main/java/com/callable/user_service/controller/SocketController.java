package com.callable.user_service.controller;

import com.callable.user_service.listener.WebSocketPresenceListener;
import com.callable.user_service.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketController {
    UserService userService;

    private final WebSocketPresenceListener presence;

    @GetMapping("/online")
    public Set<String> onlineUsers() {
        return presence.getOnlineUsers();
    }
}
