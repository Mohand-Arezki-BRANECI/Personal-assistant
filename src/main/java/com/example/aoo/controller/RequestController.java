package com.example.aoo.controller;


import com.example.aoo.dao.request.ChatRequest;
import com.example.aoo.service.impl.RequestProcessorServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RequestController {

    public final RequestProcessorServiceImpl requestProcessorImpl;
    
    public RequestController(RequestProcessorServiceImpl requestProcessorImpl) {
        this.requestProcessorImpl = requestProcessorImpl;
    }

    @PostMapping("/processRequest")
    public ResponseEntity processRequest(@RequestBody ChatRequest chatRequest) {

        return requestProcessorImpl.processRequest(chatRequest.getRequest());
    }
}

