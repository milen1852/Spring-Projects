package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Developer {

    @Autowired                                     //field injection
    @Qualifier("desktop")
    private Computer comp;
//    public Developer(Computer comp){             //Constructor Injection
//        this.comp = comp;                        //Qualifier does not work with Constructor Injection
//    }

//    @Autowired                                   //Setter Injection
//    public void setLap(Laptop lap){
//        this.lap = lap;
//    }

    public void build(){
        comp.compile();

        System.out.println("Building...");
    }
}
