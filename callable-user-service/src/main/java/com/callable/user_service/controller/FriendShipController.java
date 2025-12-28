package com.callable.user_service.controller;

import com.callable.user_service.dto.friendship.request.AddFriendShipRequestDto;
import com.callable.user_service.dto.friendship.request.RemoveFriendShipRequestDto;
import com.callable.user_service.enums.FriendStatus;
import com.callable.user_service.service.friendship.FriendShipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/friend_ship")
@Slf4j
public class FriendShipController {
    FriendShipService friendShipService;

    @PostMapping("/add")
    public void addFriend(@RequestBody AddFriendShipRequestDto addFriendShipRequestDto) {
        friendShipService.addFriend(addFriendShipRequestDto);
    }

    @PostMapping("/remove")
    public void removeFriend(@RequestBody RemoveFriendShipRequestDto removeFriendShipRequestDto) {
        friendShipService.removeFriend(removeFriendShipRequestDto);
    }

    @GetMapping("/friend")
    public ResponseEntity<?> findFriend(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam FriendStatus friendStatus) {
        return ResponseEntity.ok(friendShipService.findFriend(pageNo, pageSize, friendStatus));
    }

    @GetMapping("/unfriend")
    public ResponseEntity<?> findUnFriend(@RequestParam int pageNo, @RequestParam int pageSize) {
        return ResponseEntity.ok(friendShipService.findUnFriend(pageNo, pageSize));
    }

}
