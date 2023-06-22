package com.example.aoo.service;

import com.example.aoo.model.Command;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IServiceChatService {

    List<Command> getCommand();

    ResponseEntity processRequest( String command, String info);

    boolean matchCommand(String command);
}
