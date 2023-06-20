package com.example.aoo.service;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Service
public class RequestProcessor {
    private final DateTimeService timeService;
    private final MailService mailService;

    public RequestProcessor(DateTimeService timeService, MailService mailService) {
        this.timeService = timeService;
        this.mailService = mailService;
    }

    public ResponseEntity processRequest(String request) {
        request = request.toLowerCase();
        String [] requestSplit= request.split(" ");

        if(requestSplit[0].equals("!heure")){
            return timeService.getTime();
        }
        if(requestSplit[0].equals("!date")){
            return timeService.getDate();
        }
         if(requestSplit[0].equals("!mail")){
            return mailService.sendMail(requestSplit[1],requestSplit[2],requestSplit[3]);
         }
       

        return new  ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}