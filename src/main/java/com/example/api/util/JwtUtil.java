package com.example.api.util;

import com.example.api.dao.RefreshTokenDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {

    private static final String SECRET = "SUPER_SECURE_32_BYTE_SECRET_KEY_123456";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type","access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+15*60*1000)) // 15min
                .signWith(KEY)
                .compact();
    }

    public static String generateRefreshToken(String username) {
        String jti = UUID.randomUUID().toString();
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + 30L*24*60*60*1000); //30 days
        RefreshTokenDao.save(jti, username, expiresAt);

        return Jwts.builder()
                .setSubject(username)
                .claim("type","refresh")
                .setId(jti)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiresAt.getTime()))
                .signWith(KEY)
                .compact();
    }

    public static Claims parse(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
    }

    public static Map<String,String> rotateRefreshToken(String refreshToken) {
        try {
            Claims claims = parse(refreshToken);
            if(!"refresh".equals(claims.get("type"))) return null;

            String jti = claims.getId();
            if(!RefreshTokenDao.isValid(jti)) return null;

            RefreshTokenDao.revoke(jti);

            String username = claims.getSubject();
            String accessToken = generateAccessToken(username);
            String newRefreshToken = generateRefreshToken(username);

            Map<String,String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", newRefreshToken);
            return tokens;

        } catch (Exception e){ return null; }
    }
}

