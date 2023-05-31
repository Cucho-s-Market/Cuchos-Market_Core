package com.project.cuchosmarket.exceptions;

public class OrderNotExistException extends Exception {
    public OrderNotExistException(Long orderId) {
        super("Orden " + orderId + " no existe.");
    }
}
