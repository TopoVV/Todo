package com.topov.todo.service;

import com.topov.todo.model.User;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = { "jwt.secret=test-secret" })
@ContextConfiguration(classes = JsonTokenServiceImplTest.JsonTokenServiceTestContext.class)
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
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTM2ODI0MjUsInN1YiI6Im15bmFtZSIsImV4cCI6MTYxMzY4MzAyM30.N8lJuySqcBKhsMJ8pnNS27Dkqk5kFsRV-qqWqqXpnsY";
        assertThrows(SignatureException.class, () -> tokenService.verifyToken(token));
    }

    @Test
    public void whenContentNotValid_ThenSignatureException() {
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJssXQiOjE2MTM2OTAwNjcsInN1YiI6Im15bmFtZSIsImV4cCI6MTYxMzY5MDY2NH0.ZxJLu2h2wRM9kUQtVInFp7pbvNi2rpdc9GdNyi1JUtw";
        assertThrows(SignatureException.class, () -> tokenService.verifyToken(token));
    }

    @Test
    public void whenHeaderNotValid_ThenSignatureException() {
        final String token = "eyJskGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTM2OTAwNjcsInN1YiI6Im15bmFtZSIsImV4cCI6MTYxMzY5MDY2NH0.ZxJLu2h2wRM9kUQtVInFp7pbvNi2rpdc9GdNyi1JUtw";
        assertThrows(MalformedJwtException.class, () -> tokenService.verifyToken(token));
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
        assertThrows(ExpiredJwtException.class, () -> tokenService.verifyToken(token));
    }

    @TestConfiguration
    public static class JsonTokenServiceTestContext {
        @Bean
        public JsonTokenService jsonTokenService() {
            return new JsonTokenServiceImpl();
        }
    }
}
