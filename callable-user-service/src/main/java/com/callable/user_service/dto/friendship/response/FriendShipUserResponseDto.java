package com.callable.user_service.dto.friendship.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendShipUserResponseDto {
    Long id;
    String fullName;
    String avatar;
}
