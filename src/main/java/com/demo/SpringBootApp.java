package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApp {
    public static void main(String[] args) {
        System.out.println("启动前");
        SpringApplication.run(SpringBootApp.class, args);
        System.out.println("启动后");
    }
}
