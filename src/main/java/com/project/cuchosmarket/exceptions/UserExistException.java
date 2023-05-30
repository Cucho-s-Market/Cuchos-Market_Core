package com.project.cuchosmarket.exceptions;

public class UserExistException extends Exception {
    public UserExistException() {
        super("El usuario ya existe.");
    }
    public UserExistException(String message) {
        super(message);
    }
}
