package com.example.exception;

public class PriceFoundException extends RuntimeException{

    public PriceFoundException(String message){
        super(message);
    }
}
