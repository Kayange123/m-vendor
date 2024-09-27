package dev.kayange.Multivendor.E.commerce.security;

import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .subject(Long.toString(userPrincipal.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken() {
        // generate a random UUID as refresh token
        return UUID.randomUUID().toString();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Long getUserIdFromJWT(String token) {
        var id = extractClaim(token, Claims::getSubject);
        return Long.parseLong(id);
    }


    private Claims extractAllClaims(String token) {
        ///validateToken(token);
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String authToken) {
        try {
             extractAllClaims(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("Invalid JWT token");
            throw new ApiException("Token is expired");
        } catch (MalformedJwtException ex) {
            //logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            //logger.error("Unsupported JWT token");
        } catch (Exception ex) {
            throw new ApiException("Invalid token");
            //logger.error("JWT claims string is empty.");
        }
        return false;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
