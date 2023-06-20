package com.example.aoo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeService {

    public ResponseEntity<String> getTime() {
        String pattern = "HH:mm:ss";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedTime = currentTime.format(formatter);
        return new ResponseEntity<>(formattedTime, HttpStatus.OK);
    }

    public  ResponseEntity<String> getDate() {
        String pattern = "dd-MM-yyyy";
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = currentDate.format(formatter);
        return new ResponseEntity<>(formattedDate, HttpStatus.OK);
    }

}
