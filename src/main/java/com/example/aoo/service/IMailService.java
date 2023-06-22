package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IMailService {
    ResponseEntity<String> sendMail(String to, String subject, String content);
}
