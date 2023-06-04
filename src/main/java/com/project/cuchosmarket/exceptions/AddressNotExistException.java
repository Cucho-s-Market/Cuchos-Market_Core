package com.project.cuchosmarket.exceptions;


public class AddressNotExistException extends Exception {
    public AddressNotExistException() {
        super("Datos Invalidos.");
    }
    public AddressNotExistException(String message) {
        super(message);
    }
}