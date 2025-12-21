package com.callable.user_service.listener;

import com.callable.user_service.dto.auth.response.PresenceMessageDto;
import com.callable.user_service.enums.Presence;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketPresenceListener {

    Map<String, AtomicInteger> userSessions = new ConcurrentHashMap<>();
    SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() == null) return;
        String email = accessor.getUser().getName();
        AtomicInteger count = userSessions.computeIfAbsent(email, u -> new AtomicInteger(0));
        int current = count.incrementAndGet();
        if (current == 1) {
            log.info("🟢 ONLINE: {}", email);
            PresenceMessageDto presenceMessageDto = PresenceMessageDto.builder()
                    .email(email)
                    .presence(Presence.ONLINE)
                    .build();
            messagingTemplate.convertAndSend("/topic/presence", presenceMessageDto);
            log.info("sended");
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() == null) return;
        String email = accessor.getUser().getName();
        AtomicInteger count = userSessions.get(email);
        if (count == null) return;
        int current = count.decrementAndGet();
        if (current <= 0) {
            userSessions.remove(email);
            log.info("🔴 OFFLINE: {}", email);
            PresenceMessageDto presenceMessageDto = PresenceMessageDto.builder()
                    .email(email)
                    .presence(Presence.OFFLINE)
                    .build();
            messagingTemplate.convertAndSend("/topic/presence", presenceMessageDto);
            log.info("sended");
        }
    }

    public Set<String> getOnlineUsers() {
        return userSessions.keySet();
    }
}