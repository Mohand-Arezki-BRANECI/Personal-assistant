package com.example.aoo.service.impl;

import com.example.aoo.service.DateTimeServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Service
public class DateTimeService implements DateTimeServiceInterface {

    @Value("${time.end}")
    private String time;

@Override
    public ResponseEntity<String> getTime(String[] requestSplit) {
        if(requestSplit.length == 1 ){
            return getLocalTime();
        }
        else {
            return getTimeInCountry(requestSplit[1]);
        }
    }

    @Override
    public ResponseEntity<String> getDate(String[] requestSplit) {
        if(requestSplit.length == 1){
            return getLocalDate();
        }
        else {
            return getDateInCountry(requestSplit[1]);
        }
    }
@Override
    public ResponseEntity<String> getLocalTime() {
        String pattern = "HH:mm:ss";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeResponse = "Il est "+currentTime.format(formatter);
        return new ResponseEntity<>(timeResponse, HttpStatus.OK);
    }
@Override
    public ResponseEntity<String> getTimeInCountry(String country) {
        ZoneId countryTime = getZoneId(country);
        if (countryTime == null) {
            return new ResponseEntity<>("Pays non reconnu", HttpStatus.BAD_REQUEST);
        }
        ZonedDateTime time = ZonedDateTime.now(countryTime);

        String pattern = "HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeResponse = "Heure locale de "+country+" : "+time.format(formatter);

        return new ResponseEntity<>(timeResponse, HttpStatus.OK);
    }
@Override
    public ResponseEntity<String> getLocalDate() {
        String pattern = "dd-MM-yyyy";
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String dateResponse = "Nous sommes le "+currentDate.format(formatter);
        return new ResponseEntity<>(dateResponse, HttpStatus.OK);
    }
@Override
    public ResponseEntity<String> getDateInCountry(String country) {
        ZoneId countryDate = getZoneId(country);

        if (countryDate == null) {
            return new ResponseEntity<>("Pays non reconnu", HttpStatus.BAD_REQUEST);
        }

        ZonedDateTime date = ZonedDateTime.now(countryDate);

        String pattern = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String dateResponse = "Nous sommes le "+date.format(formatter);

        return new ResponseEntity<>(dateResponse, HttpStatus.OK);
    }

    public ZoneId getZoneId(String country) {
        String[] tz = TimeZone.getAvailableIDs();
            for (String t : tz) {
                String id = t.toLowerCase();
                if (id.equals(country)) {
                  return TimeZone.getTimeZone(t).toZoneId();
                }
            }
        return null;
    }
    @Override
 public ResponseEntity<String> getEndOfClass() {
        Timestamp timeEndOfClass = new Timestamp(Long.parseLong(time));
        Timestamp timeDiffernce = new Timestamp(timeEndOfClass.getTime()- Instant.now().toEpochMilli());
        String reponse = "";
        if (timeDiffernce.getTime() > 0 ){
            reponse =
                    "<h3>Il te reste plus que</h3>" +
                    "<h1>" + timeDiffernce.getTime()/ 86400000 + "</h1> " +
                    "<h3>jours avant la fin des cours</h3>";
        }
        else {
            reponse =
                    "<h3>La fin des cours était y a </h3>" +
                    "<h1>" + Math.abs(timeDiffernce.getTime() / 86400000) + "</h1> ";
        }
        reponse =
                "<html>" +
                        "<body>" +
                        reponse +
                    "</body>" +
                "</html>" ;
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

}
