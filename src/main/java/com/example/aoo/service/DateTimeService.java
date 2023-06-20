package com.example.aoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
public class DateTimeService {

@Value("${time.end}")
String time;



    public  ResponseEntity<String> getTime() {
        String pattern = "HH:mm:ss";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedTime = currentTime.format(formatter);
        return new ResponseEntity<>(formattedTime, HttpStatus.OK);
    }


    public ResponseEntity<String> getDate() {
        String pattern = "dd-MM-yyyy";
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = currentDate.format(formatter);
        return new ResponseEntity<>(formattedDate, HttpStatus.OK);
    }


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
