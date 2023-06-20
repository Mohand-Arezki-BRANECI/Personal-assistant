package com.example.aoo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RequestProcessor {
    private final MailService mailService;

     public RequestProcessor(MailService mailService) {
            this.mailService = mailService;
     }

        public ResponseEntity processRequette(String requette) {
            requette = requette.toLowerCase();
            String [] requetteSplit= requette.split(" ");

            if(requetteSplit[0].equals("!mail")){
             return mailService.sendMail(requetteSplit[1],requetteSplit[2],requetteSplit[3]);
            }


            return new  ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

}
