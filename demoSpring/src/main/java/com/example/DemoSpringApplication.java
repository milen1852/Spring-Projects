package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoSpringApplication {

	public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoSpringApplication.class, args);

        Developer d = context.getBean(Developer.class);

        d.build();
	}
}
