package com.callable.user_service.interceptor;

import com.callable.user_service.service.user.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JWTService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String auth = accessor.getFirstNativeHeader("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing JWT");
            }

            String token = auth.substring(7);
            String email = jwtService.extractUserName(token);

            accessor.setUser(
                    new UsernamePasswordAuthenticationToken(email, null)
            );
        }

        return message;
    }
}