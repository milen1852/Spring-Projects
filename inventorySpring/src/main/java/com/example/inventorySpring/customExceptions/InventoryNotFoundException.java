package com.example.inventorySpring.customExceptions;

public class InventoryNotFoundException extends RuntimeException{

    public InventoryNotFoundException(String message){
        super(message);
    }
}
