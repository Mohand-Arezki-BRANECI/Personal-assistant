package com.example.aoo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Service
public class DateTimeService {

    public ResponseEntity<String> getTime(String[] requestSplit) {
        if(requestSplit.length == 1 ){
            return getLocalTime();
        }
        else {
            return getTimeInCountry(requestSplit[1]);
        }
    }

    public ResponseEntity<String> getDate(String[] requestSplit) {
        if(requestSplit.length == 1){
            return getLocalDate();
        }
        else {
            return getDateInCountry(requestSplit[1]);
        }
    }

    public ResponseEntity<String> getLocalTime() {
        String pattern = "HH:mm:ss";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeResponse = "Il est "+currentTime.format(formatter);
        return new ResponseEntity<>(timeResponse, HttpStatus.OK);
    }

    public ResponseEntity<String> getTimeInCountry(String country) {
        ZoneId countryTime = getZoneId(country);

        ZonedDateTime time = ZonedDateTime.now(countryTime);

        String pattern = "HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeResponse = "Heure locale de "+country+" : "+time.format(formatter);

        return new ResponseEntity<>(timeResponse, HttpStatus.OK);
    }

    public ResponseEntity<String> getLocalDate() {
        String pattern = "dd-MM-yyyy";
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String dateResponse = "Nous sommes le "+currentDate.format(formatter);
        return new ResponseEntity<>(dateResponse, HttpStatus.OK);
    }

    public ResponseEntity<String> getDateInCountry(String country) {
        ZoneId countryDate = getZoneId(country);
        ZonedDateTime date = ZonedDateTime.now(countryDate);

        String pattern = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String dateResponse = "Nous sommes le "+date.format(formatter);

        return new ResponseEntity<>(dateResponse, HttpStatus.OK);
    }

    public ZoneId getZoneId(String country) {
        String[] tz = TimeZone.getAvailableIDs();
        ZoneId timeZone = null;

        try {
            for (String t : tz) {
                String id = t.toLowerCase();
                if (id.equals(country)) {
                    timeZone = TimeZone.getTimeZone(t).toZoneId();
                }
            }
        }catch (NullPointerException e){
            System.out.println("TimeZone du pays non trouvé");
        }
        return timeZone;
    }

}
