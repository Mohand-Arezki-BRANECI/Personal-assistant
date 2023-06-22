package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IMeteoService {
    ResponseEntity getMeteo(String[] t);
}
