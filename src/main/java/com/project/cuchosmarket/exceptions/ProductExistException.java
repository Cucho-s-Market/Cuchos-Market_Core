package com.project.cuchosmarket.exceptions;

public class ProductExistException extends Exception {
    public ProductExistException() {
        super("El producto ya existe.");
    }
    public ProductExistException(String message) {
        super(message);
    }
}
