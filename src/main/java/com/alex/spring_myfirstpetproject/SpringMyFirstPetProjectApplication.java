package com.alex.spring_myfirstpetproject;

import org.apache.catalina.Executor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class SpringMyFirstPetProjectApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(SpringMyFirstPetProjectApplication.class, args);

        System.out.println("ddd");

    }



}
