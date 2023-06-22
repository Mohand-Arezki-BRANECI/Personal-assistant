package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface DateTimeServiceInterface {
    ResponseEntity<String> getTime(String[] requestSplit);

    ResponseEntity<String> getDate(String[] requestSplit);

    ResponseEntity<String> getLocalTime();

    ResponseEntity<String> getTimeInCountry(String country);

    ResponseEntity<String> getLocalDate();

    ResponseEntity<String> getDateInCountry(String country);

    ResponseEntity<String> getEndOfClass();
}
