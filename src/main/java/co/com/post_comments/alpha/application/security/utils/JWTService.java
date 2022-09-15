package co.com.post_comments.alpha.application.security.utils;

import co.com.post_comments.alpha.application.security.models.AppUser;
import co.com.post_comments.alpha.application.security.models.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JWTService {
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(this.secretKey)
            .build();

    public JWT generateJWTForUserWithCredentials(String username, Collection<? extends GrantedAuthority> authorities) {
        List<String> authorityClaims = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorityClaims);
        return new JWT(
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(Date.from(
                                Instant.now().plus(30, ChronoUnit.DAYS)
                        ))
                        .setIssuedAt(new Date())
                        .signWith(this.secretKey)
                        .addClaims(claims)
                        .compact()
        );
    }

    public String getUsernameFromToken(JWT token) {
        return this.parser.parseClaimsJws(token.getValue())
                .getBody()
                .getSubject();
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(JWT token) {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) this.parser.parseClaimsJws(token.getValue())
                .getBody()
                .get("authorities", List.class)
                .stream().map(o -> {
                    return new SimpleGrantedAuthority((String) o);
                })
                .collect(Collectors.toList());
        return authorities;
    }

    public boolean isValidJWT(JWT token, AppUser user) {
        Claims claims = this.parser.parseClaimsJws(token.getValue())
                .getBody();
        return claims.getExpiration().after(Date.from(Instant.now())) && claims.getSubject().equals(user.getUsername());
    }
}
