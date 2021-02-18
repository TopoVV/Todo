package com.topov.todo.service;

import com.topov.todo.model.User;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class JsonTokenServiceImplTest {

    private JsonTokenService tokenService;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public JsonTokenServiceImplTest(JsonTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Test
    public void whenSignatureValid_ThenNoExceptions() {
        final User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("myname");
        final String token = this.tokenService.createAuthenticationToken(mockUser);

        final Jws<Claims> claimsJws = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token);


        assertEquals(claimsJws.getBody().getSubject(), mockUser.getUsername());
    }

    @Test
    public void whenKeyNotValid_ThenSignatureException() {
        final User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("myname");
        final String token = this.tokenService.createAuthenticationToken(mockUser);

        assertThrows(SignatureException.class, () -> {
            Jwts.parser()
                .setSigningKey("not my secret")
                .parseClaimsJws(token);
        });
    }

    @Test
    public void whenSignatureNotValid_ThenSignatureException() {
        final User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("myname");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTM2ODI0MjUsInN1YiI6Im15bmFtZSIsImV4cCI6MTYxMzY4MzAyM30.N8lJuySqcBKhsMJ8pnNS27Dkqk5kFsRV-qqWqqXpnsY";

        assertThrows(SignatureException.class, () -> {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
        });
    }

    @Test
    public void whenTokenExpired_ThenExpiredJwtException() {
        final JsonTokenService mockTokenService = mock(JsonTokenService.class);
        Calendar cal = Calendar.getInstance();

        when(mockTokenService.createAuthenticationToken(any())).thenReturn(
         Jwts.builder()
            .setIssuedAt(new Date())
            .setSubject("myname")
            .setExpiration(new Date(cal.getTimeInMillis() - (30L * 60000)))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
        );

        final String token = mockTokenService.createAuthenticationToken(mock(User.class));

        assertThrows(ExpiredJwtException.class, () -> {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
        });
    }

}
