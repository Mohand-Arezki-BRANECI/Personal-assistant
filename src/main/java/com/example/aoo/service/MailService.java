package com.example.aoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MailService {
    private final RestTemplate restTemplate;
    @Value("${mail.url}")
    private String mailUrl;

    public MailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<String> sendMail(String to, String subject, String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body
        String requestBody = "{\"recipient\": \"" + to + "\", \"subject\": \"" + subject + "\", \"content\": \"" + content + "\"}";

        // Create the HTTP entity with headers and body
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request
      return restTemplate.exchange(mailUrl, HttpMethod.POST, entity, String.class);
    }
}
