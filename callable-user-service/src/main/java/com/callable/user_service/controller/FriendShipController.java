package com.callable.user_service.controller;

import com.callable.user_service.dto.friendship.request.AddFriendShipRequestDto;
import com.callable.user_service.dto.friendship.request.RejectFriendShipRequestDto;
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
@RequestMapping("/friend-ship")
@Slf4j
public class FriendShipController {
    FriendShipService friendShipService;

    @PostMapping("/add")
    public void addFriend(@RequestBody AddFriendShipRequestDto addFriendRequestDto) {
        friendShipService.addFriend(addFriendRequestDto);
    }

    @GetMapping("/friends")
    public ResponseEntity<?> findFriendsWithFriendStatus(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam FriendStatus friendStatus) {
        return ResponseEntity.ok(friendShipService.findFriendsWithFriendStatus(pageNo, pageSize, friendStatus));
    }


    @GetMapping("/non-friends")
    public ResponseEntity<?> findNonFriends(@RequestParam int pageNo, @RequestParam int pageSize) {
        return ResponseEntity.ok(friendShipService.findNonFriends(pageNo, pageSize));
    }

    @PostMapping("/reject")
    public void rejectFriend(@RequestBody RejectFriendShipRequestDto rejectFriendShipRequestDto) {
        friendShipService.rejectFriend(rejectFriendShipRequestDto);
    }

    @DeleteMapping("/remove")
    public void removeFriend(@RequestBody RemoveFriendShipRequestDto removeFriendRequestDto) {
        friendShipService.removeFriend(removeFriendRequestDto);
    }


}
