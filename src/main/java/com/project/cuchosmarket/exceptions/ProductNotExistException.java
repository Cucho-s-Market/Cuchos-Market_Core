package com.project.cuchosmarket.exceptions;

public class ProductNotExistException extends Exception {
    public ProductNotExistException(String productName) {
        super("El producto " + productName + " no existe.");
    }
}
