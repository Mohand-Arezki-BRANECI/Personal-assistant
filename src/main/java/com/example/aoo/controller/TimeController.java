package com.example.aoo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/time")
public class TimeController {

    @GetMapping
    public String getTime() {
        String pattern = "HH:mm:ss";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
}
