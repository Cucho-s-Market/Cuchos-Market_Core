package com.project.cuchosmarket.exceptions;

public class CategoryNotExistException extends Exception{
    public CategoryNotExistException() {
        super("Categoria no encontrada.");
    }

    public CategoryNotExistException(String message) {
        super(message);
    }
}
