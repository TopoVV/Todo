package com.topov.todo.service;

import com.topov.todo.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class JsonTokenServiceImpl implements JsonTokenService {
    private final int ONE_MINUTE_IN_MILLS = 60000;

    private final Calendar calendar = Calendar.getInstance();

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String createAuthenticationToken(User user) {
        final long time = this.calendar.getTimeInMillis();

        return Jwts.builder()
            .setIssuedAt(new Date())
            .setSubject(user.getUsername())
            .setExpiration(new Date(time + (10L * ONE_MINUTE_IN_MILLS)))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();

    }

    @Override
    public void verifyToken(String token) {
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token);
    }
}
