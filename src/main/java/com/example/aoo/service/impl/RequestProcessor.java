package com.example.aoo.service.impl;


import com.example.aoo.dao.response.ErrorCommandResponse;
import com.example.aoo.model.Command;
import com.example.aoo.service.IRequestProcessor;
import com.example.aoo.service.IServiceChat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RequestProcessor implements IRequestProcessor {
    private final DateTimeService timeService;
    private final MailService mailService;
    private final MeteoService meteoService;

    public final List<IServiceChat> serviceList ;

    public RequestProcessor(DateTimeService timeService, MailService mailService,MeteoService meteoService) {
        this.timeService = timeService;
        this.mailService = mailService;
        this.meteoService = meteoService;
        this.serviceList = List.of(timeService,mailService,meteoService);
    }
    @Override
    public ResponseEntity processRequest(String request) {
        String command = request.split(" ")[0];
        String info = request.substring(command.length()).trim();

        return processService(command,info);

    }


    private ResponseEntity processService(String command, String info){
        for (IServiceChat service : serviceList) {
            if (service.matchCommand(command)) {
                return service.processRequest(command,info);
            }
        }

        return new ResponseEntity <ErrorCommandResponse>( ErrorCommandResponse.builder().triedCommand(command).l(List.of(Command.values())).message("Commande inexistante").build(), HttpStatus.BAD_REQUEST);
    }

}

