package me.silvana23.todo.core.exceptions.handlers;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;

import me.silvana23.todo.core.exceptions.BadRequestException;

import java.util.Map;

@Priority(100)
@Provider
@RequiredArgsConstructor
public class BadRequestHandler implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("message", exception.getMessage()))
                .build();
    }
}
