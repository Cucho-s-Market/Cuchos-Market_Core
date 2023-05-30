package com.project.cuchosmarket.exceptions;

public class InvalidProductException extends Exception{
    public InvalidProductException() {
        super("Informacion del producto invalida.");
    }

    public InvalidProductException(String message) {
        super(message);
    }
}
