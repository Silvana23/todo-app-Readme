package me.silvana23.todo.core.exceptions;

import java.io.Serializable;

public class NotFoundException extends RuntimeException implements Serializable {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
