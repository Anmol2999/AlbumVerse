package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class AccountController {

    @GetMapping("/")
    public String hello() {
        return "Hello, AlbumVerse!";
    }

    @GetMapping("/test")
    @Tag(name = "Test Endpoint")
    @SecurityRequirement(name = "bearerAuth")
    public String test() {
        return "Test endpoint - authenticated access only";
    }

}
