package com.topov.todo.service;

import com.topov.todo.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JsonTokenServiceImpl implements JsonTokenService {
    private final int ONE_MINUTE_IN_MILLS = 60000;

    private final Calendar calendar = Calendar.getInstance();

    private String secret;

    public JsonTokenServiceImpl(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

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
}
