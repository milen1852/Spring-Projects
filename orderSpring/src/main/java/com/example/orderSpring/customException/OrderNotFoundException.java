package com.example.orderSpring.customException;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String message){
        super(message);
    }
}
