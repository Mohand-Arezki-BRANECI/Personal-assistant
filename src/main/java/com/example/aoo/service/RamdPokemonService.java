package com.example.aoo.service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;


@Service
public class RamdPokemonService {

    private static final String API_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    int randomId = getRandomId(1, 500); //nb pokemon 500
    String apiUrl = API_BASE_URL + randomId;

    public ResponseEntity<String> getRamdPokemon() {
/*
     ModelAndView modelAndView = new ModelAndView("pokemonTemplate");
     modelAndView.addObject("url", "");
     modelAndView.addObject("name","");*/
     String reponse = ""; //modelAndView.toString();

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                                URI.create(API_BASE_URL))
                                .header("accept", "application/json")
                                .build();


// use the client to send the request
//        var response = client.send(request, new JsonBodyHandler<>(APOD.class));

        try {
            var response = client.send(request,  HttpResponse.BodyHandlers.ofString());
            System.out.println("Status  : " + response.statusCode());
            System.out.println("Headers : " + response.headers());
            System.out.println("Body    : " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }


    private int getRandomId(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}

