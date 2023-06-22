package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IMailService extends IServiceChat {
    ResponseEntity<String> sendMail(String[] requestSplit);
}
