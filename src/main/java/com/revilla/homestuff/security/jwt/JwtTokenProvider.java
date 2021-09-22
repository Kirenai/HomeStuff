package com.revilla.homestuff.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Data
@Configuration
@NoArgsConstructor
public class JwtTokenProvider {

    @Value(value = "${application.jwt.secret-key}")
    private String secretKey;
    @Value(value = "${application.jwt.token-prefix}")
    private String tokenPrefix;
    @Value(value = "${application.jwt.token-expiration-after-days}")
    private Integer tokenExpirationAfterDays;

    public String getJwtAuthenticationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String generateJwtToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now()
                        .plusDays(this.getTokenExpirationAfterDays())))
                .signWith(Keys.hmacShaKeyFor(this.getSecretKey().getBytes()))
                .compact();
    }

    public String getJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(this.getJwtAuthenticationHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(this.getTokenPrefix())) {
            return bearerToken.substring(this.getTokenPrefix().length());
        }
        return null;
    }

    public Claims getJwtBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.getSecretKey().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(this.getSecretKey().getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
