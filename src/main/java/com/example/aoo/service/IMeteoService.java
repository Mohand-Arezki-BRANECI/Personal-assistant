package com.example.aoo.service;

import org.springframework.http.ResponseEntity;

public interface IMeteoService extends IServiceChat {
    ResponseEntity getMeteo(String[] t);
}
