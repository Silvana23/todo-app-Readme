package me.silvana23.todo.core.exceptions;

import java.io.Serializable;

public class BadRequestException extends RuntimeException implements Serializable {
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
