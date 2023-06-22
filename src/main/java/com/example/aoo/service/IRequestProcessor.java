package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IRequestProcessor {
    ResponseEntity processRequest(String request);
}
