package com.example.userSpring.exceptions;

public class OrderServiceUnavailableException extends RuntimeException{

    public OrderServiceUnavailableException(String message){
        super(message);
    }
}
