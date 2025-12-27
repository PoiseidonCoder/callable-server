package com.callable.user_service.controller;

import com.callable.user_service.dto.friendship.request.AddFriendShipRequestDto;
import com.callable.user_service.dto.friendship.request.RemoveFriendShipRequestDto;
import com.callable.user_service.enums.FriendStatus;
import com.callable.user_service.service.friendship.FriendShipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/friend_ship")
public class FriendShipController {
    FriendShipService friendShipService;

    @PostMapping("/add_friend")
    public void addFriend(@RequestBody AddFriendShipRequestDto addFriendShipRequestDto) {
        friendShipService.addFriend(addFriendShipRequestDto);
    }

    @PostMapping("/remove_friend_ship")
    public void removeFriendShip(@RequestBody RemoveFriendShipRequestDto removeFriendShipRequestDto) {
        friendShipService.removeFriendShipRequestDto(removeFriendShipRequestDto);
    }

    @GetMapping("/friend")
    public ResponseEntity<?> findFriendsShip(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam FriendStatus friendStatus) {
        return ResponseEntity.ok(friendShipService.findFriendsShip(pageNo, pageSize, friendStatus));
    }

    @GetMapping("/unfriend")
    public ResponseEntity<?> findUnFriendsShip(@RequestParam int pageNo, @RequestParam int pageSize) {
        return ResponseEntity.ok(friendShipService.findUnFriendsShip(pageNo, pageSize));
    }

}
