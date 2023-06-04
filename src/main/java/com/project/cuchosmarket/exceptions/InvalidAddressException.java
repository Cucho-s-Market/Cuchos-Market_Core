package com.project.cuchosmarket.exceptions;


public class InvalidAddressException extends Exception {
    public InvalidAddressException() {
        super("La direcccion no existe.");
    }
    public InvalidAddressException(String message) {
        super(message);
    }
}