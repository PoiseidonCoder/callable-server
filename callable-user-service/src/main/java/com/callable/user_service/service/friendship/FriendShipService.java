package com.callable.user_service.service.friendship;

import com.callable.user_service.dto.common.PageResponse;
import com.callable.user_service.dto.friendship.request.AddFriendShipRequestDto;
import com.callable.user_service.dto.friendship.request.RemoveFriendShipRequestDto;
import com.callable.user_service.dto.friendship.response.FriendShipUserResponseDto;
import com.callable.user_service.enums.FriendStatus;
import com.callable.user_service.model.FriendShip;
import com.callable.user_service.repository.FriendShipRepository;
import com.callable.user_service.repository.UserRepository;
import com.callable.user_service.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipService {
    FriendShipRepository friendShipRepository;
    UserRepository userRepository;
    UserService userService;

    @Transactional
    public void addFriend(AddFriendShipRequestDto addFriendShipRequestDto) {
        Long currentUserId = userService.getCurrentUser().getId();
        Long addressee = addFriendShipRequestDto.getAddressee();
        if (currentUserId.equals(addFriendShipRequestDto.getAddressee()))
            throw new IllegalArgumentException("Cannot add yourself");
        Optional<FriendShip> friendshipOpt = friendShipRepository.findFriendShip(currentUserId, addressee);

        if (friendshipOpt.isPresent()) {
            FriendShip friendship = friendshipOpt.get();
            switch (friendship.getFriendStatus()) {
                case BLOCKED -> throw new IllegalArgumentException("User is blocked");

                case ACCEPTED -> throw new IllegalArgumentException("Already friends");

                case PENDING -> throw new IllegalArgumentException("Friend request already sent");

                default -> {
                    friendship.setFriendStatus(FriendStatus.PENDING);
                    friendship.setRequester(userRepository.getReferenceById(currentUserId));
                    friendship.setAddressee(userRepository.getReferenceById(addressee));
                    friendShipRepository.save(friendship);
                }
            }
        } else {
            FriendShip friendship = new FriendShip();
            friendship.setFriendStatus(FriendStatus.PENDING);
            friendship.setRequester(userRepository.getReferenceById(currentUserId));
            friendship.setAddressee(userRepository.getReferenceById(addressee));
            friendShipRepository.save(friendship);
        }

    }

    @Transactional
    public void removeFriend(RemoveFriendShipRequestDto removeFriendShipRequestDto) {
        Long currentUserId = userService.getCurrentUser().getId();
        Long addressee = removeFriendShipRequestDto.getAddressee();
        if (currentUserId.equals(addressee)) throw new IllegalArgumentException("Cannot add yourself");
        Optional<FriendShip> friendshipOpt = friendShipRepository.findFriendShip(currentUserId, addressee);

        if (friendshipOpt.isPresent()) {
            FriendShip friendship = friendshipOpt.get();
            switch (friendship.getFriendStatus()) {
                case BLOCKED -> throw new IllegalArgumentException("User is blocked");
                case PENDING, ACCEPTED -> friendShipRepository.delete(friendship);
            }
        } else throw new IllegalArgumentException("No friendship exists");
    }

    public PageResponse<FriendShipUserResponseDto> findFriend(int pageNo, int pageSize, FriendStatus friendStatus) {
        Long currentUserId = userService.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return PageResponse.from(friendShipRepository.findFriendsShip(currentUserId, FriendStatus.ACCEPTED, pageable));
    }

    public PageResponse<FriendShipUserResponseDto> findUnFriend(int pageNo, int pageSize) {
        Long currentUserId = userService.getCurrentUser().getId();

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return PageResponse.from(friendShipRepository.findUnFriendShip(currentUserId, pageable));
    }

}
