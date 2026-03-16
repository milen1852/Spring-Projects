package com.example.userSpring.exceptions;

public class UserExistsException extends RuntimeException{

    public UserExistsException(String message){
        super(message);
    }
}
