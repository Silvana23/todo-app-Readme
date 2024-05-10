package me.silvana23.todo.core.exceptions.handlers;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;
import me.silvana23.todo.core.exceptions.AlreadyExistsException;

import java.util.Map;

@Priority(100)
@Provider
@RequiredArgsConstructor
public class AlreadyExistsHandler implements ExceptionMapper<AlreadyExistsException> {
    @Override
    public Response toResponse(AlreadyExistsException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("message", "Resource already exists."))
                .build();
    }
}
