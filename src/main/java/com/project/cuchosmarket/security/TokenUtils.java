package com.project.cuchosmarket.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private final static String ACCESS_TOKEN_SECRET = "4pE8z3PBoHjnV1AhvGk+e8h2p+ShZpOnpr8cwHmMh1w=";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2592000L; //30 dias

    public static String createToken(String name, String email) {
        long expTime = ACCESS_TOKEN_VALIDITY_SECONDS*1000;
        Date expDate = new Date(System.currentTimeMillis() + expTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", name);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }
    }
}
