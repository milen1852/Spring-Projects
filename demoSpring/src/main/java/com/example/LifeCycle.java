package com.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

//@Component
public class LifeCycle {

    @PreDestroy
    public void destroy(){
        System.out.println("Destroyed");       //Executes only after executing every blocks
    }

    @PostConstruct
    public void init(){
        System.out.println("In init method");   //Executes next after the constructor block
    }

    public LifeCycle(){
        System.out.println("In constructor");
    }

    public String greet(){
        return "Hello Spring";
    }
}
