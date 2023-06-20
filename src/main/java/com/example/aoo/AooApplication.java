package com.example.aoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AooApplication {


    public static void main(String[] args) {
        SpringApplication.run(AooApplication.class, args);
    }

}
