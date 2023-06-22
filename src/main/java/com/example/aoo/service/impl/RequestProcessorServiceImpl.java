package com.example.aoo.service.impl;


import com.example.aoo.dao.response.ErrorCommandResponse;
import com.example.aoo.model.Command;
import com.example.aoo.service.IRequestProcessorService;
import com.example.aoo.service.IServiceChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RequestProcessorServiceImpl implements IRequestProcessorService {
    private final DateServiceTimeServiceImpl timeService;
    private final MailServiceImpl mailServiceImpl;
    private final MeteoServiceImpl meteoServiceImpl;

    public final List<IServiceChatService> serviceList ;

    public RequestProcessorServiceImpl(DateServiceTimeServiceImpl timeService, MailServiceImpl mailServiceImpl, MeteoServiceImpl meteoServiceImpl) {
        this.timeService = timeService;
        this.mailServiceImpl = mailServiceImpl;
        this.meteoServiceImpl = meteoServiceImpl;
        this.serviceList = List.of(timeService, mailServiceImpl, meteoServiceImpl);
    }
    @Override
    public ResponseEntity processRequest(String request) {
        String command = request.split(" ")[0];
        String info = request.substring(command.length()).trim();

        return processService(command,info);

    }


    private ResponseEntity processService(String command, String info){
        for (IServiceChatService service : serviceList) {
            if (service.matchCommand(command)) {
                return service.processRequest(command,info);
            }
        }

        return new ResponseEntity <ErrorCommandResponse>( ErrorCommandResponse.builder().triedCommand(command).l(List.of(Command.values())).message("Commande inexistante").build(), HttpStatus.BAD_REQUEST);
    }

}

