package me.silvana23.todo.core.exceptions;

import java.io.Serializable;

public class AuthorizationException extends RuntimeException implements Serializable {
    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
