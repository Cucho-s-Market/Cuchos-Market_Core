package com.project.cuchosmarket.exceptions;

public class InvalidPromotionException extends Exception {
    public InvalidPromotionException(String message) {
        super(message);
    }

    public InvalidPromotionException() {
        super("Datos invalidos.");
    }
}
