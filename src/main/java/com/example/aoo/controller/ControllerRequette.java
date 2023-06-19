package com.example.aoo.controller;

import com.example.aoo.service.RequetteProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ControllerRequette {
    public final RequetteProcessor requetteProcessor;

public ControllerRequette(RequetteProcessor requetteProcessor) {
        this.requetteProcessor = requetteProcessor;
    }

    @PostMapping("/processRequette")
    public ResponseEntity processRequest(@RequestBody String request) {
       return requetteProcessor.processRequette(request);
    }



}
