package com.servesync.exception;

/**
 * Exception thrown when a user's role is not found in the system.
 */
public class UserRoleNotFoundException extends RuntimeException {

    public UserRoleNotFoundException() {
        super("User role not found.");
    }

    public UserRoleNotFoundException(String message) {
        super(message);
    }

    public UserRoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
