package com.project.cuchosmarket.exceptions;

public class UserNotExistException extends Exception {
    public UserNotExistException() {
        super("El usuario no existe.");
    }
    public UserNotExistException(String message) {
        super(message);
    }
}
