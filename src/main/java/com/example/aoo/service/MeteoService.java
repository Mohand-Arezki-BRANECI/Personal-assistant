package com.example.aoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MeteoService {
    private final RestTemplate restTemplate;

    @Value("${meteo_url}")
    private String meteoURL;
    public MeteoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity getMeteo(String [] t){

        float f1 = Float.parseFloat(t[1]);
        float f2 = Float.parseFloat(t[2]);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity <String> entity = new HttpEntity<>(headers);
        meteoURL+="?latitude="+f1+"&longitude="+f2+"&current_weather=true";
        return restTemplate.exchange(meteoURL,HttpMethod.GET,entity,String.class);
    }

}