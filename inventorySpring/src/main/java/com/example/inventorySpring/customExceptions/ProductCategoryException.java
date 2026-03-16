package com.example.inventorySpring.customExceptions;

public class ProductCategoryException extends RuntimeException{
    public ProductCategoryException(String message){
        super(message);
    }
}
