package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IServiceMeteoService extends IServiceChatService {
    ResponseEntity getMeteo(String[] t);
}
