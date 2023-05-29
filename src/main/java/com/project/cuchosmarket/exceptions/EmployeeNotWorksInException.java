package com.project.cuchosmarket.exceptions;

public class EmployeeNotWorksInException extends Exception{
    public EmployeeNotWorksInException() {
        super("Empleado no trabaja para esta sucursal.");
    }

    public EmployeeNotWorksInException(String message) {
        super(message);
    }
}
