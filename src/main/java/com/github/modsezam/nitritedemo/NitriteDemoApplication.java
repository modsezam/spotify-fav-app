package com.github.modsezam.nitritedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NitriteDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NitriteDemoApplication.class, args);
    }

}
