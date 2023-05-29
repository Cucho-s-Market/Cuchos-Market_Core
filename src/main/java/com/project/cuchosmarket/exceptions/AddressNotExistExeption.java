package com.project.cuchosmarket.exceptions;


public class AddressNotExistExeption extends Exception {
    public AddressNotExistExeption() {
        super("La direcccion no existe.");
    }
    public AddressNotExistExeption(String message) {
        super(message);
    }
}