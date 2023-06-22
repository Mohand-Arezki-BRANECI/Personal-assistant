package com.example.aoo.service.impl;

import com.example.aoo.model.Command;
import com.example.aoo.service.IMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailService implements IMailService {
    private final RestTemplate restTemplate;
    @Value("${mail.url}")
    private String mailUrl;

    public MailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

@Override
    public ResponseEntity<String> sendMail(String[] requestSplit) {
        String to = requestSplit[0];
        String subject = requestSplit[1];
        String content = requestSplit[2];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body
        String requestBody = "{\"recipient\": \"" + to + "\", \"subject\": \"" + subject + "\", \"content\": \"" + content + "\"}";

        // Create the HTTP entity with headers and body
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request
      return restTemplate.exchange(mailUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public List<Command> getCommand() {
        List<Command> c = new ArrayList<>();
        c.add(Command.MAIL);
        return c;
    }
    @Override
    public ResponseEntity processRequest(String command, String info) {
        String [] requestSplit = info.split(" ");
        if (command.equals(Command.MAIL.getValue())) {
            return sendMail(requestSplit);
        }
        return new ResponseEntity<>("Commande non reconnue", HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean matchCommand(String command) {
        for (Command c : getCommand()) {
            if (c.getValue().equals(command)) {
                return true;
            }
        }
        return false;
    }
}
