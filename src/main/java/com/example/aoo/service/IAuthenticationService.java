package com.example.aoo.service;


import com.example.aoo.dao.request.SignUpRequest;
import com.example.aoo.dao.request.SigninRequest;
import com.example.aoo.dao.response.JwtAuthenticationResponse;

public interface IAuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}