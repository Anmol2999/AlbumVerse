package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.AlbumVerse.payload.auth.Token;
import com.example.AlbumVerse.payload.auth.UserLogin;
import com.example.AlbumVerse.service.TokenService;

import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
@ResponseBody
public Token token(@RequestBody UserLogin userLogin) throws AuthenticationException {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password())
    );
    return new Token(tokenService.generateToken(authentication));
}
}
