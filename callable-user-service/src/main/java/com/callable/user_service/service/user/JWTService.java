package com.callable.user_service.service.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTService {

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.accessExpirationMs}")
    Long accessExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    Long refreshExpirationMs;

    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    private static final String CLAIM_TYPE = "type";

    private String generateToken(Long userId, String type, Long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TYPE, type);

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationTime))
                .signWith(getKey())
                .compact();
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, TOKEN_TYPE_ACCESS, accessExpirationMs);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, TOKEN_TYPE_REFRESH, refreshExpirationMs);
    }

    public Long extractUserId(String token) {
        String sub = extractClaim(token, Claims::getSubject);
        return sub == null ? null : Long.valueOf(sub);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, c -> c.get(CLAIM_TYPE, String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        if (token == null || userDetails == null) return false;
        if (isTokenExpired(token)) return false;

        if (!TOKEN_TYPE_ACCESS.equals(extractTokenType(token))) return false;

        if (userDetails instanceof CustomUserDetails custom) {
            return extractUserId(token).equals(custom.getId());
        }

        return false;
    }

    public boolean validateRefreshToken(String token, Long expectedUserId) {
        if (token == null || expectedUserId == null) return false;
        if (isTokenExpired(token)) return false;

        return TOKEN_TYPE_REFRESH.equals(extractTokenType(token))
                && expectedUserId.equals(extractUserId(token));
    }

    public boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp == null || exp.before(new Date());
    }

    public Long getAccessTokenExpirationTime() {
        return System.currentTimeMillis() + accessExpirationMs;
    }

    public Long getRefreshTokenExpirationTime() {
        return System.currentTimeMillis() + refreshExpirationMs;
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public interface CustomUserDetails extends UserDetails {
        Long getId();
    }
}
