package com.callable.user_service.interceptor;

import com.callable.user_service.enums.AuthToken;
import com.callable.user_service.service.user.JWTService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtChannelInterceptor implements ChannelInterceptor {

    JWTService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String auth = accessor.getFirstNativeHeader("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                return null;
            }

            String token = auth.substring(7);

            if (jwtService.isTokenExpired(token)) {
                return null;
            }

            if (!AuthToken.ACCESS.equals(jwtService.extractTokenType(token))) {
                return null;
            }

            Long userId = jwtService.extractUserId(token);
            if (userId == null) {
                return null;
            }

            accessor.setUser(
                    new UsernamePasswordAuthenticationToken(
                            String.valueOf(userId),
                            null,
                            null
                    )
            );
        }

        return message;
    }
}
