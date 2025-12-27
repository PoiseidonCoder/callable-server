package com.callable.user_service.model;

import com.callable.user_service.enums.FriendStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "friendships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "addressee_id"}))
public class FriendShip {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Users requester;

    @ManyToOne
    @JoinColumn(name = "addressee_id")
    private Users addressee;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    private LocalDateTime createdAt;
}