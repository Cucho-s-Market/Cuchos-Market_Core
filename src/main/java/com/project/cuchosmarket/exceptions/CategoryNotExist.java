package com.project.cuchosmarket.exceptions;

public class CategoryNotExist extends Exception{
    public CategoryNotExist() {
        super("Categoria no encontrada.");
    }

    public CategoryNotExist(String message) {
        super(message);
    }
}
