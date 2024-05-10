package me.silvana23.todo.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import me.silvana23.todo.core.exceptions.AuthenticationException;
import me.silvana23.todo.core.security.JWTUtil;
import me.silvana23.todo.core.security.PasswordUtil;
import me.silvana23.todo.entities.User;
import org.jboss.logging.Logger;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {
    private final Logger logger;

    private final UserService userService;
    private final PasswordUtil passwordUtil;
    private final JWTUtil jwtUtil;

    public Uni<UUID> signUp(User user) {
        return userService.create(user)
                .onItem()
                .ifNotNull()
                .transform(User::getId);
    }

    public Uni<String> signIn(User user) {
        return userService.getByUsername(user.getUsername())
                .onItem().ifNull()
                .failWith(new AuthenticationException("Invalid credentials."))
                .onItem().ifNotNull()
                .transformToUni(userEntity -> {
                    if (!passwordUtil.verify(userEntity.getPassword(), user.getPassword())) {
                        return Uni.createFrom().failure(new AuthenticationException("Invalid credentials."));
                    }

                    return jwtUtil.createJsonWebToken(userEntity);
                });
    }
}
