package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class HomeController {

    @GetMapping("/")
    public String hello() {
        return "Hello, AlbumVerse!";
    }

    @GetMapping("/test")
    @Tag(name = "Test Endpoint")

    public String test() {
        return "Test endpoint - authenticated access only";
    }

}
