package com.example.aoo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RequetteProcessor {
    private final MailService mailService;
    private final MeteoService meteoService;

     public RequetteProcessor(MailService mailService, MeteoService meteoService) {
            this.mailService = mailService;
         this.meteoService = meteoService;
     }

        public ResponseEntity processRequette(String requette) {
            requette = requette.toLowerCase();
            String [] requetteSplit= requette.split(" ");

            if(requetteSplit[0].equals("!mail")){
             return mailService.sendMail(requetteSplit[1],requetteSplit[2],requetteSplit[3]);
            }
            if (requetteSplit[0].equals("!meteo")){
                return meteoService.getMeteo(requetteSplit);
            }


            return new  ResponseEntity<>(new HttpHeaders(),HttpStatus.BAD_REQUEST);
        }

}
