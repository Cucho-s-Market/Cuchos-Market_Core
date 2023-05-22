package com.project.cuchosmarket.exceptions;

public class ProductInvalidException extends Exception{
    public ProductInvalidException() {
        super("Informacion del producto invalida.");
    }

    public ProductInvalidException(String message) {
        super(message);
    }
}
