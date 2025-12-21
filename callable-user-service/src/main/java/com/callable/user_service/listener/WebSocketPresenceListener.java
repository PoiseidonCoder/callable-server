package com.callable.user_service.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketPresenceListener {

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        var accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() != null) {
            String user = accessor.getUser().getName();
            onlineUsers.add(user);
            log.info("🟢 ONLINE: {}", user);
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        var accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() != null) {
            String user = accessor.getUser().getName();
            onlineUsers.remove(user);
            log.info("🔴 OFFLINE: {}", user);
        }
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }
}
