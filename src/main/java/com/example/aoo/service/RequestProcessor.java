package com.example.aoo.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Service
public class RequestProcessor {
    private final DateTimeService timeService;

    public RequestProcessor(DateTimeService timeService) {
        this.timeService = timeService;
    }

    public ResponseEntity processRequest(String request) {
        request = request.toLowerCase();
        String [] requestSplit= request.split(" ");

        if(requestSplit[0].equals("!heure")){
            return timeService.getTime(requestSplit);
        }
        if(requestSplit[0].equals("!date")){
            return timeService.getDate(requestSplit);
        }

        return new  ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}

