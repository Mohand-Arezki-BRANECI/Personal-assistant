package com.example.aoo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RequestProcessor {
    private final MailService mailService;
    private final MeteoService meteoService;

     public RequestProcessor(MailService mailService, MeteoService meteoService) {
            this.mailService = mailService;
         this.meteoService = meteoService;
     }

        public ResponseEntity processRequest(String request) {
            request = request.toLowerCase();
            String [] requestSplit= request.split(" ");

            if(requestSplit[0].equals("!mail")){
             return mailService.sendMail(requestSplit[1],requestSplit[2],requestSplit[3]);
            }
            if (requestSplit[0].equals("!meteo")){
                return meteoService.getMeteo(requestSplit);
            }


            return new  ResponseEntity<>(new HttpHeaders(),HttpStatus.BAD_REQUEST);
        }

}
