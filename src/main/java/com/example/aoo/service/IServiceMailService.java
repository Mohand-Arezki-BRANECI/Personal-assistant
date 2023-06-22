package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IServiceMailService extends IServiceChatService {
    ResponseEntity<String> sendMail(String[] requestSplit);
}
