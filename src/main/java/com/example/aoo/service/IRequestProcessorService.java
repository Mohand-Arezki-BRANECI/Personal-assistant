package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IRequestProcessorService {
    ResponseEntity processRequest(String request);
}
