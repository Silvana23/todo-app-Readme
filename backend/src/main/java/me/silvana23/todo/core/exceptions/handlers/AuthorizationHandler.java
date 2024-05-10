package me.silvana23.todo.core.exceptions.handlers;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import me.silvana23.todo.core.exceptions.AuthenticationException;

import java.util.Map;

@Priority(100)
@Provider
@RequiredArgsConstructor
public class AuthorizationHandler implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException exception) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("message", exception.getMessage()))
                .build();
    }
}
