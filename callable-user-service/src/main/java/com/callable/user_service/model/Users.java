package com.callable.user_service.model;

import com.callable.user_service.enums.AuthProvider;
import com.callable.user_service.enums.Gender;
import com.callable.user_service.enums.Marital;
import com.callable.user_service.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(unique = true, length = 100)
    String email;

    @Column(nullable = false)
    String password;

    @Column
    String avatar;

    @Column
    String fullName;


    @Column
    String biography;

    @Column
    Gender gender;

    @Column
    Date birthDate;

    @Column
    Integer phoneNumber;

    @Column
    Marital marital;

    @Column
    Instant lastOnline;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    Set<Role> role = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AuthProvider provider = AuthProvider.LOCAL;

    @Column(name = "provider_id")
    String providerId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .toList();
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
