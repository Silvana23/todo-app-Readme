package me.silvana23.todo.core.exceptions;

import java.io.Serializable;

public class AlreadyExistsException extends RuntimeException implements Serializable {
    public AlreadyExistsException() {}

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
