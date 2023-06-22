package com.example.aoo.service.impl;

import com.example.aoo.model.Command;
import com.example.aoo.service.IServiceMeteoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeteoServiceImpl implements IServiceMeteoService {
    private final RestTemplate restTemplate;

    @Value("${meteo_url}")
    private String meteoURL;
    public MeteoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public ResponseEntity getMeteo(String[] t){

        float f1 = Float.parseFloat(t[0]);
        float f2 = Float.parseFloat(t[1]);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity <String> entity = new HttpEntity<>(headers);
        meteoURL+="?latitude="+f1+"&longitude="+f2+"&current_weather=true";
        return restTemplate.exchange(meteoURL,HttpMethod.GET,entity,String.class);
    }

    @Override
    public List<Command> getCommand() {
        List<Command> c = new ArrayList<>();
        c.add(Command.METEO);
        return c;
    }
    @Override
    public ResponseEntity processRequest(String command, String info) {
        String [] requestSplit = info.split(" ");
        if (command.equals(Command.METEO.getValue())) {
            return getMeteo(requestSplit);
        }
        return new ResponseEntity<>("Commande non implementé", HttpStatus.BAD_REQUEST);
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