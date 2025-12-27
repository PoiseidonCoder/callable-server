package com.callable.user_service.repository;

import com.callable.user_service.dto.friendship.response.FriendShipUserResponseDto;
import com.callable.user_service.enums.FriendStatus;
import com.callable.user_service.model.FriendShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("""
                SELECT f FROM FriendShip f
                WHERE (f.requester.id = :requesterId AND f.addressee.id = :addresseeId)
                   OR (f.requester.id = :addresseeId AND f.addressee.id = :requesterId)
            """)
    Optional<FriendShip> findFriendShip(Long requesterId, Long addresseeId);


    @Query(
            value = """
                        select new com.callable.user_service.dto.friendship.response.FriendShipUserResponseDto(
                            case when fs.requester.id = :userId then fs.addressee.id else fs.requester.id end,
                            case when fs.requester.id = :userId then fs.addressee.fullName else fs.requester.fullName  end,
                            case when fs.requester.id = :userId then fs.addressee.avatar else fs.requester.avatar end
                        )
                        from FriendShip fs
                        where
                            (fs.requester.id = :userId or fs.addressee.id = :userId)
                            and fs.friendStatus = :friendStatus
                    """,
            countQuery =
                    """
                                select count(fs.id)
                                from FriendShip fs
                                where
                                    (fs.requester.id = :requester or fs.addressee.id = :requester)
                                    and fs.friendStatus = :friendStatus
                            """
    )
    Page<FriendShipUserResponseDto> findFriendsShip(
            @Param("userId") Long requester,
            FriendStatus friendStatus,
            Pageable pageable
    );

    @Query(
            value = """
                        select new com.callable.user_service.dto.friendship.response.FriendShipUserResponseDto(
                            u.id,
                            u.fullName,
                            u.avatar
                        )
                        from Users u
                        where u.id <> :userId
                        and not exists (
                            select 1
                            from FriendShip fs
                            where
                                (fs.requester.id = :userId and fs.addressee.id = u.id)
                                or
                                (fs.requester.id = u.id and fs.addressee.id = :userId)
                        )
                    """,
            countQuery = """
                        select count(u.id)
                        from Users u
                        where u.id <> :userId
                        and not exists (
                            select 1
                            from FriendShip fs
                            where
                                (fs.requester.id = :userId and fs.addressee.id = u.id)
                                or
                                (fs.requester.id = u.id and fs.addressee.id = :userId)
                        )
                    """
    )
    Page<FriendShipUserResponseDto> findUnFriendShip(
            @Param("userId") Long userId,
            Pageable pageable
    );

}
