package com.example.aoo.controller;

import com.example.aoo.dto.RequestDto;
import com.example.aoo.service.impl.RequestProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ControllerRequest {

    public final RequestProcessor requestProcessor;
    
    public ControllerRequest(RequestProcessor requetteProcessor) {
        this.requestProcessor = requetteProcessor;
    }

    @PostMapping("/processRequest")
    public ResponseEntity processRequest(@RequestBody RequestDto requestDTO) {

        return requestProcessor.processRequest(requestDTO.getRequest());
    }
}

