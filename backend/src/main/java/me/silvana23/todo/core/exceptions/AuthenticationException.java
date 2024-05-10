package me.silvana23.todo.core.exceptions;

import java.io.Serializable;

public class AuthenticationException extends RuntimeException implements Serializable {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
