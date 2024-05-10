package me.silvana23.todo.core.exceptions.handlers;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import me.silvana23.todo.core.exceptions.AlreadyExistsException;
import me.silvana23.todo.core.exceptions.NotFoundException;

import java.util.Map;

@Priority(100)
@Provider
@RequiredArgsConstructor
public class NotFoundHandler implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("message", "Resource not found."))
                .build();
    }
}
