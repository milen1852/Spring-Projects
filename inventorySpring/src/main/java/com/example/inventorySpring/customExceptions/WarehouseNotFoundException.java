package com.example.inventorySpring.customExceptions;

public class WarehouseNotFoundException extends RuntimeException{

    public WarehouseNotFoundException(String message){
        super(message);
    }
}
