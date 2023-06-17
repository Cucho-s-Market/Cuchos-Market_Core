package com.project.cuchosmarket.exceptions;

public class PromotionNotExistException extends Exception {
    public PromotionNotExistException() {
        super("Esta promocion no existe.");
    }
}
