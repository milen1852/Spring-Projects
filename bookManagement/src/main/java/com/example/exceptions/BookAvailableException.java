package com.example.exceptions;

public class BookAvailableException extends RuntimeException{
    public BookAvailableException(String message){
        super(message);
    }
}
