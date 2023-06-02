package com.project.cuchosmarket.exceptions;

public class BranchNotExistException extends Exception {
    public BranchNotExistException(Long branchId) {
        super("La sucursal con la id " + branchId + " no existe");
    }
}
