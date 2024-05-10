package me.silvana23.todo.core.security;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtSignatureException;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import me.silvana23.todo.entities.User;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.time.Instant;

@Singleton
@RequiredArgsConstructor
public class JWTUtil {
    private final Logger logger;

    public Uni<String> createJsonWebToken(User user) {
        return Uni.createFrom().deferred(() -> {
            try {
                var token = Jwt.issuer("https://lmorais.dev/issuer")
                        .issuedAt(Instant.now())
                        .audience("https://todo.lmorais.dev")
                        .subject(user.getUsername())
                        .upn(user.getUsername())
                        .expiresIn(Duration.ofHours(1))
                        .groups(user.getRole().getName())
                        .claim("userId", user.getId())
                        .sign();

                return Uni.createFrom().item(token);
            } catch (JwtSignatureException e) {
                logger.error("JWT Creation Error\n", e);
                return Uni.createFrom().failure(InternalServerErrorException::new);
            }
        });
    }
}
