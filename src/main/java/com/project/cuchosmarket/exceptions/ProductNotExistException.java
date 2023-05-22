package com.project.cuchosmarket.exceptions;

public class ProductNotExistException extends Exception {
    public ProductNotExistException() {
        super("El producto no existe.");
    }
    public ProductNotExistException(String message) {
        super(message);
    }
}
