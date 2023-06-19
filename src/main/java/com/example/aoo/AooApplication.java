package com.example.aoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AooApplication {
    @GetMapping
    public String welcom(){
        return "welcome to google";
    }

    public static void main(String[] args) {
        SpringApplication.run(AooApplication.class, args);
    }

}
