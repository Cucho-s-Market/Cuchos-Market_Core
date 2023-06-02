package com.project.cuchosmarket.exceptions;

public class InvalidCategoryException extends Exception{
    public InvalidCategoryException() {
        super("Información de categoria invalida.");
    }

    public InvalidCategoryException(String message) {
        super(message);
    }
}
