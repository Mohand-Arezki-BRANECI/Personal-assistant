package com.example.aoo.service.impl;

import com.example.aoo.dao.request.SignUpRequest;
import com.example.aoo.dao.request.SigninRequest;
import com.example.aoo.dao.response.JwtAuthenticationResponse;
import com.example.aoo.model.Role;
import com.example.aoo.repository.UserRepository;
import com.example.aoo.service.IAuthenticationService;
import com.example.aoo.service.IJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.aoo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService IJwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().name(request.getFirstName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        var jwt = IJwtService.generateToken(user);
        userRepository.save(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = IJwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}