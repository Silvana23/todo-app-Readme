package me.silvana23.todo.core.security.filters;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;
import me.silvana23.todo.entities.User;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.util.UUID;

@Priority(100)
@Provider
@RequiredArgsConstructor
public class AuthenticationFilter {
    private static final Response UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();

    private final JsonWebToken token;

    @WithSession
    @ServerRequestFilter(preMatching = true)
    public Uni<Response> preMatchingFilter(ContainerRequestContext requestContext) {
        String userId = token.getClaim("userId");

        try {
            UUID id = UUID.fromString(userId);

            return User.exists(id)
                    .map(exists -> {
                        if (!exists) return UNAUTHORIZED;
                        return null;
                    });
        } catch (IllegalArgumentException | NullPointerException e) {
            return Uni.createFrom().nullItem();
        }
    }
}
