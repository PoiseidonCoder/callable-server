package com.callable.user_service.dto.auth.response;

import com.callable.user_service.enums.Presence;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresenceMessageDto {
    Long userId;
    Presence presence;
}
